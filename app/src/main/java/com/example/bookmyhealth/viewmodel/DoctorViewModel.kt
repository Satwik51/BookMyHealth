package com.example.bookmyhealth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookmyhealth.data.model.Doctor
import com.example.bookmyhealth.data.repository.FirebaseRepository

class DoctorViewModel : ViewModel() {

    private val repository = FirebaseRepository()

    private val _doctorList = MutableLiveData<List<Doctor>>()
    val doctorList: LiveData<List<Doctor>> get() = _doctorList

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    /** Fetch doctors from repository */
    fun fetchDoctors() {
        _loading.value = true
        _error.value = null

        repository.getAllDoctors { doctors, errorMsg ->

            _loading.postValue(false)

            if (errorMsg != null) {
                // Error occurred
                _error.postValue(errorMsg)
                _doctorList.postValue(emptyList())
                return@getAllDoctors
            }

            // Success
            if (doctors != null && doctors.isNotEmpty()) {
                _doctorList.postValue(doctors)
            } else {
                _doctorList.postValue(emptyList())
                _error.postValue("No doctors found.")
            }
        }
    }
}
