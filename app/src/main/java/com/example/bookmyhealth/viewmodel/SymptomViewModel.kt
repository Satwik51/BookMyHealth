package com.example.bookmyhealth.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookmyhealth.data.model.*
import com.example.bookmyhealth.utils.JsonUtils
import kotlin.math.ln
import kotlin.math.roundToInt

class SymptomViewModel : ViewModel() {

    private val _symptoms = MutableLiveData<List<Symptom>>()
    val symptoms: LiveData<List<Symptom>> = _symptoms

    private val _filteredSymptoms = MutableLiveData<List<Symptom>>()
    val filteredSymptoms: LiveData<List<Symptom>> = _filteredSymptoms

    private val _selectedSymptoms = MutableLiveData<MutableList<Symptom>>(mutableListOf())
    val selectedSymptoms: LiveData<MutableList<Symptom>> = _selectedSymptoms

    private val _selectedCategory = MutableLiveData<SymptomCategory?>(null)
    val selectedCategory: LiveData<SymptomCategory?> = _selectedCategory

    private val _searchQuery = MutableLiveData<String>("")
    val searchQuery: LiveData<String> = _searchQuery

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _analysisResults = MutableLiveData<List<AnalysisResult>>()
    val analysisResults: LiveData<List<AnalysisResult>> = _analysisResults

    private val _analysisCount = MutableLiveData<Int>(0)
    val analysisCount: LiveData<Int> = _analysisCount

    private var diseaseDatabase: List<Disease> = emptyList()

    fun init(context: Context) {
        diseaseDatabase = JsonUtils.loadDiseases(context)
        loadSymptomsFromDiseases()
    }

    private fun loadSymptomsFromDiseases() {
        val allSymptoms = diseaseDatabase
            .flatMap { it.symptoms }
            .distinct()
            .sorted()
            .mapIndexed { index, name ->
                Symptom(
                    id = "symptom_$index",
                    name = name,
                    category = categorizeSymptom(name),
                    isSelected = false,
                    severity = 1
                )
            }

        _symptoms.value = allSymptoms
        _filteredSymptoms.value = allSymptoms
    }

    private fun categorizeSymptom(name: String): SymptomCategory {
        val lower = name.lowercase()
        return when {
            lower.contains("pain") || lower.contains("ache") ||
                    lower.contains("sore") || lower.contains("hurt") -> SymptomCategory.PAIN

            lower.contains("nausea") || lower.contains("vomit") ||
                    lower.contains("diarrhea") || lower.contains("stomach") ||
                    lower.contains("abdominal") || lower.contains("bowel") ||
                    lower.contains("digest") || lower.contains("appetite") -> SymptomCategory.DIGESTIVE

            lower.contains("cough") || lower.contains("breath") ||
                    lower.contains("wheez") || lower.contains("lung") ||
                    lower.contains("throat") || lower.contains("nose") ||
                    lower.contains("sneez") || lower.contains("congestion") -> SymptomCategory.RESPIRATORY

            lower.contains("rash") || lower.contains("itch") ||
                    lower.contains("skin") || lower.contains("red") ||
                    lower.contains("swell") -> SymptomCategory.SKIN

            lower.contains("head") || lower.contains("dizzi") ||
                    lower.contains("vision") || lower.contains("memory") ||
                    lower.contains("confusion") || lower.contains("numb") ||
                    lower.contains("tingling") || lower.contains("seizure") -> SymptomCategory.NEUROLOGICAL

            lower.contains("fever") || lower.contains("fatigue") ||
                    lower.contains("chill") || lower.contains("sweat") ||
                    lower.contains("weight") || lower.contains("tired") ||
                    lower.contains("weak") || lower.contains("appetite") -> SymptomCategory.GENERAL

            else -> SymptomCategory.OTHER
        }
    }

    fun selectCategory(category: SymptomCategory?) {
        _selectedCategory.value = category
        filterSymptoms()
    }

    fun searchSymptoms(query: String) {
        _searchQuery.value = query
        filterSymptoms()
    }

    private fun filterSymptoms() {
        val allSymptoms = _symptoms.value ?: return
        val category = _selectedCategory.value
        val query = _searchQuery.value ?: ""

        var filtered = allSymptoms

        if (category != null) {
            filtered = filtered.filter { it.category == category }
        }

        if (query.isNotEmpty()) {
            filtered = filtered.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }

        _filteredSymptoms.value = filtered
    }

    fun toggleSymptomSelection(symptom: Symptom) {
        val currentList = _selectedSymptoms.value ?: mutableListOf()
        val existingIndex = currentList.indexOfFirst { it.id == symptom.id }

        if (existingIndex >= 0) {
            currentList.removeAt(existingIndex)
        } else {
            currentList.add(symptom.copy(isSelected = true, severity = 1))
        }

        _selectedSymptoms.value = currentList
        updateSymptomSelectionStates()
    }

    fun updateSymptomSeverity(symptomId: String, severity: Int) {
        val currentList = _selectedSymptoms.value ?: return
        val index = currentList.indexOfFirst { it.id == symptomId }

        if (index >= 0) {
            currentList[index] = currentList[index].copy(severity = severity)
            _selectedSymptoms.value = currentList
        }
    }

    fun removeSymptom(symptomId: String) {
        val currentList = _selectedSymptoms.value ?: return
        currentList.removeAll { it.id == symptomId }
        _selectedSymptoms.value = currentList
        updateSymptomSelectionStates()
    }

    fun clearAllSelections() {
        _selectedSymptoms.value?.clear()
        _selectedSymptoms.value = _selectedSymptoms.value
        updateSymptomSelectionStates()
    }

    private fun updateSymptomSelectionStates() {
        val selectedIds = _selectedSymptoms.value?.map { it.id }?.toSet() ?: emptySet()
        val allSymptoms = _symptoms.value ?: return

        _symptoms.value = allSymptoms.map { symptom ->
            symptom.copy(isSelected = selectedIds.contains(symptom.id))
        }
        filterSymptoms()
    }

    fun analyzeSymptoms() {
        val selected = _selectedSymptoms.value ?: return
        if (selected.size < 3) return

        _isLoading.value = true

        val inputSymptoms = selected.map { it.name }
        val results = analyzeWithAI(inputSymptoms)

        _analysisResults.value = results
        _analysisCount.value = (_analysisCount.value ?: 0) + 1
        _isLoading.value = false
    }

    private fun analyzeWithAI(inputSymptoms: List<String>): List<AnalysisResult> {
        val inputSet = inputSymptoms.map { it.lowercase() }.toSet()

        val symptomFrequency = mutableMapOf<String, Int>()
        for (disease in diseaseDatabase) {
            for (symptom in disease.symptoms) {
                val key = symptom.lowercase()
                symptomFrequency[key] = (symptomFrequency[key] ?: 0) + 1
            }
        }

        val totalDiseases = diseaseDatabase.size
        val results = mutableListOf<AnalysisResult>()

        for (disease in diseaseDatabase) {
            val diseaseSet = disease.symptoms.map { it.lowercase() }.toSet()
            val matched = inputSet.intersect(diseaseSet)

            if (matched.isEmpty()) continue

            var score = 0.0

            for (symptom in matched) {
                val freq = symptomFrequency[symptom] ?: 1
                val weight = ln((totalDiseases.toDouble() / freq) + 1)
                score += weight * 2
            }

            val missing = diseaseSet - inputSet
            val extra = inputSet - diseaseSet

            score -= missing.size * 0.5
            score -= extra.size * 0.3

            val maxPossible = diseaseSet.size * 2.5
            var normalized = score / maxPossible

            val matchRatio = matched.size.toDouble() / diseaseSet.size
            if (matchRatio > 0.6) {
                normalized += 0.15
            }

            normalized = normalized.coerceIn(0.0, 1.0)
            val percentage = (normalized * 100).roundToInt()

            if (percentage > 15) {
                results.add(
                    AnalysisResult(
                        diseaseName = disease.name,
                        confidence = percentage,
                        matchedSymptoms = matched.toList(),
                        doctorType = getDoctorType(disease.name)
                    )
                )
            }
        }

        return results.sortedByDescending { it.confidence }.take(5)
    }

    private fun getDoctorType(diseaseName: String): String {
        return when {
            diseaseName.contains("Asthma", true) ||
                    diseaseName.contains("Bronchitis", true) ||
                    diseaseName.contains("Pneumonia", true) ||
                    diseaseName.contains("Lung", true) ||
                    diseaseName.contains("Respiratory", true) -> "Pulmonologist"

            diseaseName.contains("Skin", true) ||
                    diseaseName.contains("Allergy", true) ||
                    diseaseName.contains("Rash", true) ||
                    diseaseName.contains("Itch", true) -> "Dermatologist"

            diseaseName.contains("Heart", true) ||
                    diseaseName.contains("Hypertension", true) ||
                    diseaseName.contains("Blood Pressure", true) -> "Cardiologist"

            diseaseName.contains("Diabetes", true) ||
                    diseaseName.contains("Thyroid", true) -> "Endocrinologist"

            diseaseName.contains("Brain", true) ||
                    diseaseName.contains("Migraine", true) ||
                    diseaseName.contains("Neurological", true) ||
                    diseaseName.contains("Seizure", true) -> "Neurologist"

            diseaseName.contains("Stomach", true) ||
                    diseaseName.contains("Gastri", true) ||
                    diseaseName.contains("Digestive", true) ||
                    diseaseName.contains("Liver", true) -> "Gastroenterologist"

            diseaseName.contains("Kidney", true) ||
                    diseaseName.contains("Urinary", true) -> "Nephrologist"

            diseaseName.contains("Joint", true) ||
                    diseaseName.contains("Arthritis", true) ||
                    diseaseName.contains("Bone", true) -> "Orthopedist"

            else -> "General Physician"
        }
    }

    fun getSelectedCount(): Int = _selectedSymptoms.value?.size ?: 0
}