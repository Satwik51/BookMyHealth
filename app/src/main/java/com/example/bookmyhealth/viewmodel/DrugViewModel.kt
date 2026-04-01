package com.example.bookmyhealth.viewmodel

import androidx.lifecycle.*
import com.example.bookmyhealth.data.model.Drug
import com.example.bookmyhealth.data.repository.DrugRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DrugViewModel : ViewModel() {

    private val repo = DrugRepository()

    // ── Results ───────────────────────────────────────────────────────────────
    private val _drugData = MutableLiveData<Drug?>()
    val drugData: LiveData<Drug?> = _drugData

    // ── Suggestions ───────────────────────────────────────────────────────────
    private val _suggestions = MutableLiveData<List<String>>()
    val suggestions: LiveData<List<String>> = _suggestions

    // ── Loading ───────────────────────────────────────────────────────────────
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    // ── Error ─────────────────────────────────────────────────────────────────
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    // ── Debounce job for suggestions ──────────────────────────────────────────
    private var suggestionJob: Job? = null

    // ─── SEARCH DRUG ──────────────────────────────────────────────────────────
    fun searchDrug(query: String) {

        val cleanQuery = query.trim().lowercase()
        if (cleanQuery.isEmpty()) return

        viewModelScope.launch {

            _loading.postValue(true)
            _error.postValue(null)
            _drugData.postValue(null) // clear stale card immediately

            try {
                val response = repo.getDrug(cleanQuery)

                if (response.isSuccessful) {

                    val results = response.body()?.results.orEmpty()

                    if (results.isEmpty()) {
                        _error.postValue("No medicine found for \"$cleanQuery\"")
                        _loading.postValue(false)
                        return@launch
                    }

                    // Priority 1 — exact brand name match
                    val exactMatch = results.find { drug ->
                        drug.openfda?.brand_name
                            ?.any { it.equals(cleanQuery, ignoreCase = true) } == true
                    }

                    // Priority 2 — brand name contains query
                    val partialMatch = results.find { drug ->
                        drug.openfda?.brand_name
                            ?.any { it.lowercase().contains(cleanQuery) } == true
                    }

                    // Priority 3 — purpose/usage mentions the query
                    val usageMatch = results.find { drug ->
                        drug.purpose?.any {
                            it.lowercase().contains(cleanQuery)
                        } == true
                    }

                    // Priority 4 — indications_and_usage fallback
                    val indicationsMatch = results.find { drug ->
                        drug.indications_and_usage?.any {
                            it.lowercase().contains(cleanQuery)
                        } == true
                    }

                    val finalResult = exactMatch
                        ?: partialMatch
                        ?: usageMatch
                        ?: indicationsMatch
                        ?: results.firstOrNull()

                    _drugData.postValue(finalResult)

                } else {
                    _error.postValue("Server error (${response.code()}). Try again.")
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _error.postValue("Network error. Check your connection.")
            }

            _loading.postValue(false)
        }
    }

    // ─── SUGGESTIONS (debounced 300ms) ────────────────────────────────────────
    fun fetchSuggestions(query: String) {

        val cleanQuery = query.trim()

        if (cleanQuery.length < 2) {
            _suggestions.postValue(emptyList())
            return
        }

        // Cancel previous pending suggestion request
        suggestionJob?.cancel()

        suggestionJob = viewModelScope.launch {
            delay(300L) // debounce — avoids hammering API on every keystroke

            try {
                val result = repo.getSuggestions(cleanQuery)

                val cleanList = result
                    .map { it.trim() }
                    .filter { it.isNotBlank() }
                    .distinct()
                    .sorted()
                    .take(6)

                _suggestions.postValue(cleanList)

            } catch (e: Exception) {
                e.printStackTrace()
                _suggestions.postValue(emptyList())
            }
        }
    }

    // ─── CLEAR ERROR (call after showing Snackbar/Toast) ─────────────────────
    fun clearError() {
        _error.postValue(null)
    }
}