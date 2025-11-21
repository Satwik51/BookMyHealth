package com.example.bookmyhealth.data.model

/**
 * ✅ Doctor Model (Latest Version)
 * Fully compatible with Firebase Realtime Database + Your New Profile UI
 */
data class Doctor(

    var uid: String = "",                   // Firebase UID

    var name: String = "",                  // Doctor full name
    var email: String = "",                 // Email (readonly)
    var specialization: String = "",        // e.g., Cardiologist
    var experience: String = "",            // Experience in years
    var availableSlots: String = "",        // Slot timings (string)

    var imageUrl: String = "",              // Profile image URL (Firebase Storage)

    // ⭐ NEW FIELDS YOU ADDED IN PROFILE UI
    var about: String = "",                 // Short bio
    var clinicName: String = "",            // Clinic name
    var consultationFee: String = "",       // Fee per appointment
    var achievements: String = "",          // Awards / achievements

    // ⭐ Weekly Availability (Mon–Sun)
    var weeklyAvailability: List<String> = listOf(),

    // 🔥 Keeps compatibility with login filters
    var role: String = "Doctor"
)
