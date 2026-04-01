package com.example.bookmyhealth.network

import android.util.Log
import com.example.bookmyhealth.utils.Constants
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class GeminiService {

    private val TAG = "GEMINI_DEBUG"

    private val client = OkHttpClient.Builder()
        .connectTimeout(Constants.NETWORK_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(Constants.NETWORK_TIMEOUT, TimeUnit.SECONDS)
        .build()

    fun getResponse(
        userMessage: String,
        callback: (String) -> Unit
    ) {

        val url =
            "${Constants.GEMINI_BASE_URL}${Constants.GEMINI_MODEL}:generateContent?key=${Constants.GEMINI_API_KEY}"

        Log.d(TAG, "🚀 URL: $url")

        try {

            val finalPrompt = """
${Constants.SYSTEM_PROMPT}

User: $userMessage
            """.trimIndent()

            Log.d(TAG, "🧠 Prompt: $finalPrompt")

            val json = JSONObject()

            val parts = JSONArray()
            val partObj = JSONObject()
            partObj.put("text", finalPrompt)
            parts.put(partObj)

            val contentObj = JSONObject()
            contentObj.put("parts", parts)

            val contents = JSONArray()
            contents.put(contentObj)

            json.put("contents", contents)

            Log.d(TAG, "📦 Request Body: $json")

            val body = RequestBody.create(
                "application/json".toMediaTypeOrNull(),
                json.toString()
            )

            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

            client.newCall(request).enqueue(object : Callback {

                override fun onFailure(call: Call, e: IOException) {
                    Log.e(TAG, "❌ Network Error: ${e.message}")
                    callback("⚠️ Network error: ${e.message}")
                }

                override fun onResponse(call: Call, response: Response) {

                    Log.d(TAG, "📡 Response Code: ${response.code}")

                    val res = response.body?.string()

                    Log.d(TAG, "📩 Raw Response: $res")

                    if (!response.isSuccessful) {
                        callback("❌ API ERROR ${response.code}\n$res")
                        return
                    }

                    if (res.isNullOrEmpty()) {
                        callback("⚠️ Empty response from AI")
                        return
                    }

                    try {
                        val jsonRes = JSONObject(res)

                        val text = jsonRes
                            .getJSONArray("candidates")
                            .getJSONObject(0)
                            .getJSONObject("content")
                            .getJSONArray("parts")
                            .getJSONObject(0)
                            .getString("text")

                        callback(text.trim())

                    } catch (e: Exception) {
                        Log.e(TAG, "❌ Parsing Error: ${e.message}")
                        callback("⚠️ Parsing error\n$res")
                    }
                }
            })

        } catch (e: Exception) {
            Log.e(TAG, "❌ Unexpected Error: ${e.message}")
            callback("⚠️ Unexpected error: ${e.message}")
        }
    }
}