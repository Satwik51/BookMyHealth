package com.example.bookmyhealth.data.model

data class ChatMessage(

    val message: String,

    // 👤 true = user | false = AI
    val isUser: Boolean,

    // 🕒 Timestamp
    val time: Long = System.currentTimeMillis(),

    // 🔄 Typing / loading message (AI thinking)
    val isLoading: Boolean = false

)