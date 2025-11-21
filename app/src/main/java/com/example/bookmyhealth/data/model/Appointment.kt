package com.example.bookmyhealth.data.model

// ✅ Data model for both doctor & user side appointment representation
data class Appointment(
    val appointmentId: String = "",   // ✅ Capitalized for consistent naming
    val userId: String = "",
    val userName: String = "",
    val doctorId: String = "",
    val doctorName: String = "",
    val date: String = "",
    val time: String = "",
    val slot: String = "",               // ⭐ NEW - Slot chips added
    val status: String = "Pending"    // ✅ Default ensures non-null & consistent
)
