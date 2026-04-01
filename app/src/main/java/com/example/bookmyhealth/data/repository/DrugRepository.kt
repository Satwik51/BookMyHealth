package com.example.bookmyhealth.data.repository

import com.example.bookmyhealth.data.model.Drug
import com.example.bookmyhealth.data.model.DrugResponse
import com.example.bookmyhealth.network.RetrofitClient
import retrofit2.Response

class DrugRepository {

    private val apiKey = "aedUqxeVlfAXz7C2OQSgbsfEOCPfn85uNBmSWGpA"

    // ── MAIN SEARCH (3-STEP FALLBACK) ─────────────────────────────────────────
    suspend fun getDrug(query: String): Response<DrugResponse> {

        val clean = query.trim().lowercase()
        if (clean.isEmpty()) return emptyResponse()

        return try {

            // STEP 1 — exact quoted match on brand OR generic name
            val exactResponse = RetrofitClient.api.searchDrug(
                search = "openfda.brand_name:\"$clean\" OR openfda.generic_name:\"$clean\"",
                apiKey  = apiKey,
                limit   = 10
            )
            if (exactResponse.hasResults()) return exactResponse

            // STEP 2 — prefix/wildcard match (handles partial typing)
            val wildcardResponse = RetrofitClient.api.searchDrug(
                search = "openfda.brand_name:${clean}* OR openfda.generic_name:${clean}*",
                apiKey  = apiKey,
                limit   = 10
            )
            if (wildcardResponse.hasResults()) return wildcardResponse

            // STEP 3 — broad full-text fallback across all label fields
            RetrofitClient.api.searchDrug(
                search = clean,
                apiKey  = apiKey,
                limit   = 10
            )

        } catch (e: Exception) {
            e.printStackTrace()
            emptyResponse()
        }
    }

    // ── FETCH BY SET_ID (for history / bookmarks) ─────────────────────────────
    suspend fun fetchById(setId: String): Drug? {
        return try {
            val response = RetrofitClient.api.fetchById(
                search = "id:\"$setId\"",
                apiKey = apiKey,
                limit  = 1
            )
            if (response.isSuccessful) {
                response.body()?.results?.firstOrNull()
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // ── SUGGESTIONS ───────────────────────────────────────────────────────────
    suspend fun getSuggestions(query: String): List<String> {

        val clean = query.trim().lowercase()
        if (clean.length < 2) return emptyList()

        return try {
            val response = RetrofitClient.api.searchSuggestions(
                search = "openfda.brand_name:${clean}*",
                limit  = 8          // slightly more to survive dedup/filtering
            )

            if (response.isSuccessful) {
                response.body()?.results
                    ?.mapNotNull { it.openfda?.brand_name?.firstOrNull() }
                    ?.map { it.trim() }
                    ?.filter { it.isNotBlank() }
                    ?.distinctBy { it.lowercase() }   // case-insensitive dedup
                    ?.sortedBy { it.lowercase() }
                    ?.take(6)
                    ?: emptyList()
            } else emptyList()

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    // ── HELPERS ───────────────────────────────────────────────────────────────

    /** Returns true if the response is successful and contains at least 1 result. */
    private fun Response<DrugResponse>.hasResults(): Boolean =
        isSuccessful && !body()?.results.isNullOrEmpty()

    /** Type-safe empty response — avoids null crashes in the ViewModel. */
    private fun emptyResponse(): Response<DrugResponse> =
        Response.success(DrugResponse(results = emptyList(), meta = null))
}