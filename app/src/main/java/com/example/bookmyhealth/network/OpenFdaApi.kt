package com.example.bookmyhealth.network

import com.example.bookmyhealth.data.model.DrugResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenFdaApi {

    // ── MAIN DRUG SEARCH ─────────────────────────────────────────────────────
    // Used by DrugRepository.getDrug() — returns full label fields
    // api_key is optional but raises rate limit from 40/min to 240/min
    @GET("drug/label.json")
    suspend fun searchDrug(
        @Query("search") search: String,
        @Query("api_key") apiKey: String,
        @Query("limit") limit: Int = 10,
        @Query("skip") skip: Int = 0       // pagination-ready for future use
    ): Response<DrugResponse>

    // ── REAL-TIME SUGGESTIONS ────────────────────────────────────────────────
    // Lightweight — no api_key needed, only brand names are parsed from result
    // Kept separate so we never accidentally expose the key in suggestion calls
    @GET("drug/label.json")
    suspend fun searchSuggestions(
        @Query("search") search: String,
        @Query("limit") limit: Int = 8     // slightly more headroom for dedup
    ): Response<DrugResponse>

    // ── FETCH BY EXACT SET_ID ────────────────────────────────────────────────
    // Future use: deep-link directly to a known drug record (e.g. from history)
    @GET("drug/label.json")
    suspend fun fetchById(
        @Query("search") search: String,   // caller passes "id:\"<set_id>\""
        @Query("api_key") apiKey: String,
        @Query("limit") limit: Int = 1
    ): Response<DrugResponse>
}