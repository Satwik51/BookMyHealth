package com.example.bookmyhealth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmyhealth.data.repository.FirebaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository = FirebaseRepository()

    // ---------------------------------------------------------------------
    // 🔹 LiveData for UI State
    // ---------------------------------------------------------------------
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _successMessage = MutableLiveData<String?>()
    val successMessage: LiveData<String?> get() = _successMessage


    // ---------------------------------------------------------------------
    // ⭐ COMMON HANDLER (Avoids duplicate code)
    // ---------------------------------------------------------------------
    private fun handleAuthRequest(
        block: (callback: (Boolean, String) -> Unit) -> Unit,
        callback: (Boolean, String) -> Unit
    ) {
        _loading.value = true
        _error.value = null
        _successMessage.value = null

        viewModelScope.launch(Dispatchers.IO) {
            try {
                block { success, message ->
                    _loading.postValue(false)

                    if (success) {
                        _successMessage.postValue(message)
                    } else {
                        _error.postValue(message)
                    }

                    callback(success, message)
                }
            } catch (e: Exception) {
                val msg = "Unexpected error: ${e.message}"
                _loading.postValue(false)
                _error.postValue(msg)
                callback(false, msg)
            }
        }
    }

    // ---------------------------------------------------------------------
    // ⭐ REGISTER USER
    // ---------------------------------------------------------------------
    fun registerUser(
        name: String,
        email: String,
        password: String,
        role: String,
        callback: (Boolean, String) -> Unit
    ) {
        handleAuthRequest(
            block = { repoCallback ->
                repository.registerUser(name, email, password, role, repoCallback)
            },
            callback = callback
        )
    }

    // ---------------------------------------------------------------------
    // ⭐ LOGIN USER (Email/Password)
    // ---------------------------------------------------------------------
    fun loginUser(
        email: String,
        password: String,
        role: String,
        callback: (Boolean, String) -> Unit
    ) {
        handleAuthRequest(
            block = { repoCallback ->
                repository.loginUser(email, password, role, repoCallback)
            },
            callback = callback
        )
    }

    // ---------------------------------------------------------------------
    // ⭐ RESET PASSWORD
    // ---------------------------------------------------------------------
    fun resetPassword(email: String, callback: (Boolean, String) -> Unit) {
        handleAuthRequest(
            block = { repoCallback ->
                repository.resetPassword(email, repoCallback)
            },
            callback = callback
        )
    }

    // ---------------------------------------------------------------------
    // ⭐ GOOGLE LOGIN / SIGNUP (VERY IMPORTANT)
    // ---------------------------------------------------------------------
    fun loginWithGoogle(
        idToken: String,
        role: String,
        callback: (Boolean, String) -> Unit
    ) {
        handleAuthRequest(
            block = { repoCallback ->
                repository.signInWithGoogleIdToken(idToken, role, repoCallback)
            },
            callback = callback
        )
    }

    // ---------------------------------------------------------------------
    // ⭐ AUTO LOGIN CHECK
    // ---------------------------------------------------------------------
    fun isUserLoggedIn(): Boolean = repository.isUserLoggedIn()

    // ---------------------------------------------------------------------
    // ⭐ LOGOUT
    // ---------------------------------------------------------------------
    fun logout() = repository.logout()
}
