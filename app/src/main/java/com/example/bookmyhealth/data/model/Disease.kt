package com.example.bookmyhealth.data.model

data class Disease(
    val name: String,
    val symptoms: List<String>
)

data class Symptom(
    val id: String,
    val name: String,
    val category: SymptomCategory,
    var isSelected: Boolean = false,
    var severity: Int = 1
)

enum class SymptomCategory(val displayName: String) {
    GENERAL("General"),
    PAIN("Pain"),
    DIGESTIVE("Digestive"),
    RESPIRATORY("Respiratory"),
    SKIN("Skin"),
    NEUROLOGICAL("Neurological"),
    OTHER("Other")
}

data class AnalysisResult(
    val diseaseName: String,
    val confidence: Int,
    val matchedSymptoms: List<String>,
    val doctorType: String
)