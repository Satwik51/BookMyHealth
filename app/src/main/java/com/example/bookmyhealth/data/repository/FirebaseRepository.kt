package com.example.bookmyhealth.data.repository

import com.example.bookmyhealth.data.model.Appointment
import com.example.bookmyhealth.data.model.Doctor
import com.example.bookmyhealth.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*

class FirebaseRepository {

    private val auth = FirebaseAuth.getInstance()

    // Correct Firebase DB instance (your region URL)
    private val database = FirebaseDatabase.getInstance(
        "https://bookmyhealth-5920a-default-rtdb.asia-southeast1.firebasedatabase.app/"
    ).reference


    // -------------------------------------------------------------------------
    // ⭐ REGISTER USER (Email / Password)
    // -------------------------------------------------------------------------
    fun registerUser(
        name: String,
        email: String,
        password: String,
        role: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (!task.isSuccessful) {
                    callback(false, "Auth Error: ${task.exception?.message}")
                    return@addOnCompleteListener
                }

                val uid = auth.currentUser?.uid ?: return@addOnCompleteListener

                val userData = User(
                    uid = uid,
                    name = name,
                    email = email,
                    role = role.lowercase(),
                    imageUrl = ""               // No photo during email/password signup
                )

                // Save user under /users
                database.child("users").child(uid).setValue(userData)
                    .addOnSuccessListener {

                        // If doctor → also add under /doctors
                        if (role.lowercase() == "doctor") {
                            database.child("doctors").child(uid).setValue(userData)
                        }

                        callback(true, "Registration Successful")
                    }
                    .addOnFailureListener {
                        callback(false, "DB Error: ${it.message}")
                    }
            }
    }


    // -------------------------------------------------------------------------
    // ⭐ LOGIN USER (Email / Password)
    // -------------------------------------------------------------------------
    fun loginUser(
        email: String,
        password: String,
        role: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (!task.isSuccessful) {
                    callback(false, "Login failed: ${task.exception?.message}")
                    return@addOnCompleteListener
                }

                val uid = auth.currentUser?.uid ?: return@addOnCompleteListener

                // verify role
                database.child("users").child(uid).get()
                    .addOnSuccessListener { snap ->

                        val userRole = snap.child("role").value?.toString()?.lowercase()

                        if (userRole == role.lowercase()) {
                            callback(true, "Login Successful")
                        } else {
                            auth.signOut()
                            callback(false, "Role mismatch! You are not registered as $role")
                        }
                    }
                    .addOnFailureListener {
                        callback(false, "Error verifying role: ${it.message}")
                    }
            }
    }


    // -------------------------------------------------------------------------
    // ⭐ GOOGLE LOGIN / SIGNUP (WITH PHOTO URL)
    // -------------------------------------------------------------------------
    fun signInWithGoogleIdToken(
        idToken: String,
        role: String,
        callback: (Boolean, String) -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->

                if (!task.isSuccessful) {
                    callback(false, "Google Sign-in Failed: ${task.exception?.message}")
                    return@addOnCompleteListener
                }

                val firebaseUser = auth.currentUser
                if (firebaseUser == null) {
                    callback(false, "Error: Firebase user is null")
                    return@addOnCompleteListener
                }

                val uid = firebaseUser.uid
                val name = firebaseUser.displayName ?: ""
                val email = firebaseUser.email ?: ""
                val photoUrl = firebaseUser.photoUrl?.toString() ?: ""   // ⭐ Google Photo URL

                val userData = User(
                    uid = uid,
                    name = name,
                    email = email,
                    role = role.lowercase(),
                    imageUrl = photoUrl
                )

                // -------------------------------
                // Save to /users
                // -------------------------------
                database.child("users").child(uid).get()
                    .addOnSuccessListener { snapshot ->

                        if (!snapshot.exists()) {
                            database.child("users").child(uid).setValue(userData)
                        } else {
                            // ⭐ Update photo if missing
                            database.child("users").child(uid).child("imageUrl").setValue(photoUrl)
                        }

                        // ⭐ DOCTOR ROLE EXTRA SAVE
                        if (role.lowercase() == "doctor") {
                            database.child("doctors").child(uid).get()
                                .addOnSuccessListener { snap2 ->
                                    if (!snap2.exists()) {
                                        database.child("doctors").child(uid).setValue(userData)
                                    } else {
                                        // Update existing doctor photo
                                        database.child("doctors").child(uid).child("imageUrl").setValue(photoUrl)
                                    }
                                }
                        }

                        callback(true, "Google Login Successful")
                    }
                    .addOnFailureListener {
                        callback(false, "DB Error: ${it.message}")
                    }
            }
    }


    // -------------------------------------------------------------------------
    // ⭐ FETCH ALL DOCTORS
    // -------------------------------------------------------------------------
    fun getAllDoctors(callback: (List<Doctor>, String?) -> Unit) {

        database.child("doctors")
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    if (!snapshot.exists()) {
                        callback(emptyList(), "No doctors found")
                        return
                    }

                    val list = snapshot.children.mapNotNull { child ->
                        child.getValue(Doctor::class.java)
                            ?.copy(uid = child.key ?: "")
                    }

                    callback(list, null)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(emptyList(), error.message)
                }
            })
    }


    // -------------------------------------------------------------------------
    // ⭐ ADD APPOINTMENT
    // -------------------------------------------------------------------------
    fun addAppointment(
        appointment: Appointment,
        callback: (Boolean, String) -> Unit
    ) {
        val id = database.child("appointments").push().key

        if (id == null) {
            callback(false, "Failed to generate appointment ID")
            return
        }

        val newAppointment = appointment.copy(appointmentId = id)

        database.child("appointments").child(id)
            .setValue(newAppointment)
            .addOnSuccessListener { callback(true, "Appointment Booked!") }
            .addOnFailureListener { callback(false, it.message ?: "Failed") }
    }


    // -------------------------------------------------------------------------
    // ⭐ FETCH DOCTOR APPOINTMENTS
    // -------------------------------------------------------------------------
    fun getAppointmentsForDoctor(
        doctorId: String,
        callback: (List<Appointment>) -> Unit
    ) {
        database.child("appointments")
            .orderByChild("doctorId").equalTo(doctorId)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = snapshot.children.mapNotNull {
                        it.getValue(Appointment::class.java)
                    }
                    callback(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(emptyList())
                }
            })
    }


    // -------------------------------------------------------------------------
    // ⭐ UPDATE APPOINTMENT STATUS
    // -------------------------------------------------------------------------
    fun updateAppointmentStatus(
        appointmentId: String,
        status: String,
        callback: (Boolean) -> Unit
    ) {
        database.child("appointments").child(appointmentId).child("status")
            .setValue(status)
            .addOnCompleteListener { callback(it.isSuccessful) }
    }


    // -------------------------------------------------------------------------
    // ⭐ RESET PASSWORD
    // -------------------------------------------------------------------------
    fun resetPassword(email: String, callback: (Boolean, String) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener { callback(true, "Reset link sent") }
            .addOnFailureListener { callback(false, it.message ?: "Failed") }
    }


    // -------------------------------------------------------------------------
    // ⭐ LOGIN CHECK & LOGOUT
    // -------------------------------------------------------------------------
    fun isUserLoggedIn(): Boolean = auth.currentUser != null
    fun logout() = auth.signOut()
}
