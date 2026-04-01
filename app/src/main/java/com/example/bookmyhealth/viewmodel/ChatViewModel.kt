package com.example.bookmyhealth.viewmodel

import androidx.lifecycle.*
import com.example.bookmyhealth.data.model.ChatMessage
import com.example.bookmyhealth.data.repository.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val repository = ChatRepository()

    // 📩 Messages (IMMUTABLE SAFE)
    private val _messages = MutableLiveData<List<ChatMessage>>(emptyList())
    val messages: LiveData<List<ChatMessage>> = _messages

    // ⌨️ Typing indicator
    private val _typing = MutableLiveData<Boolean>()
    val typing: LiveData<Boolean> = _typing

    // 🚀 SEND MESSAGE
    fun sendMessage(userText: String) {

        val currentList = _messages.value.orEmpty().toMutableList()

        // 👤 Add user message
        currentList.add(ChatMessage(userText, true))
        _messages.value = currentList

        // ⌨️ Show typing
        _typing.value = true

        viewModelScope.launch(Dispatchers.IO) {

            val response = try {
                repository.getAiResponse(userText)
            } catch (e: Exception) {
                "⚠️ Something went wrong. Try again."
            }

            val updatedList = _messages.value.orEmpty().toMutableList()
            updatedList.add(ChatMessage(response, false))

            _messages.postValue(updatedList)

            // ⌨️ Hide typing
            _typing.postValue(false)
        }
    }
}