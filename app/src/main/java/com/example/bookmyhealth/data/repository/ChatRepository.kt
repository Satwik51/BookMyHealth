package com.example.bookmyhealth.data.repository

import com.example.bookmyhealth.network.GeminiService
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class ChatRepository {

    private val geminiService = GeminiService()

    suspend fun getAiResponse(userMessage: String): String {

        return suspendCancellableCoroutine { continuation ->

            geminiService.getResponse(userMessage) { response ->

                if (continuation.isActive) {
                    continuation.resume(response ?: "⚠️ No response")
                }
            }
        }
    }
}