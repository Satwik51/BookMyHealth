package com.example.bookmyhealth.utils
object Constants {

    const val GEMINI_API_KEY = "AIzaSyCX0hhnX2aQZ4z6-WF-QO4LrUunZRcbNCk"

    const val GEMINI_BASE_URL =
        "https://generativelanguage.googleapis.com/v1beta/models/"

    // 🔥 FINAL FIX (IMPORTANT)
    const val GEMINI_MODEL = "gemini-2.5-flash"

    const val NETWORK_TIMEOUT = 30L

    const val SYSTEM_PROMPT = """
You are an intelligent AI medical assistant.

- Analyze symptoms
- Suggest possible conditions
- Recommend doctor
- Keep answers simple & safe
"""
}