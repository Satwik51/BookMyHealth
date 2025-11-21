package com.example.bookmyhealth.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookmyhealth.data.model.Appointment
import com.google.firebase.database.*

class AppointmentViewModel : ViewModel() {

    private val dbRef = FirebaseDatabase
        .getInstance("https://bookmyhealth-5920a-default-rtdb.asia-southeast1.firebasedatabase.app")
        .getReference("appointments")

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _appointments = MutableLiveData<List<Appointment>>()
    val appointments: LiveData<List<Appointment>> get() = _appointments

    /** 🔥 Fetch appointments for a USER */
    fun getAppointmentsForUser(userId: String) {
        _loading.value = true

        dbRef.orderByChild("userId").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val list = snapshot.children.mapNotNull {
                        it.getValue(Appointment::class.java)
                    }

                    if (list.isEmpty()) {
                        _error.value = "No appointments found"
                    }

                    // Optional: sort by timestamp if exists
                    // list.sortedByDescending { it.timestamp }

                    _appointments.value = list
                    _loading.value = false
                }

                override fun onCancelled(error: DatabaseError) {
                    _loading.value = false
                    _error.value = "Error loading appointments: ${error.message}"
                }
            })
    }


    /** 🔥 Fetch appointments for a DOCTOR (optional enhancement) */
    fun getAppointmentsForDoctor(doctorId: String) {
        _loading.value = true

        dbRef.orderByChild("doctorId").equalTo(doctorId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val list = snapshot.children.mapNotNull {
                        it.getValue(Appointment::class.java)
                    }

                    if (list.isEmpty()) {
                        _error.value = "No appointments found"
                    }

                    _appointments.value = list
                    _loading.value = false
                }

                override fun onCancelled(error: DatabaseError) {
                    _loading.value = false
                    _error.value = error.message
                }
            })
    }
}
