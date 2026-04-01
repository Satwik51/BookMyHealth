package com.example.bookmyhealth.ui.auth.user

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bookmyhealth.R
import com.example.bookmyhealth.databinding.ActivityBookAppointmentBinding
import com.example.bookmyhealth.ui.dialog.BookingConfirmDialog
import com.example.bookmyhealth.utils.SuperToast
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class BookAppointmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookAppointmentBinding
    private val auth by lazy { FirebaseAuth.getInstance() }

    private val db by lazy {
        FirebaseDatabase.getInstance(
            "https://bookmyhealth-5920a-default-rtdb.asia-southeast1.firebasedatabase.app"
        ).reference
    }

    private var doctorId = ""
    private var doctorName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDoctorInfoFromIntent()
        fetchDoctorFullDetails()

        setupDatePicker()
        setupTimePicker()
        setupBookButton()

        startEntryAnimations()
    }

    // ------------------------------------------------------------
    private fun startEntryAnimations() {
        val views = listOf(
            binding.tvDoctorName,
            binding.tvSpecialization,
            binding.tvExperience,
            binding.tvAboutDoctor,
            binding.tvClinicInfo,
            binding.layoutWeekAvailability,
            binding.chipSlots
        )

        var delay = 0L
        for (v in views) {
            animateSlide(v, delay)
            delay += 120
        }

        animatePop(binding.etDate, 450)
        animatePop(binding.etTime, 550)
    }

    private fun animateSlide(view: View, delay: Long) {
        view.alpha = 0f
        view.translationY = -40f

        view.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(450)
            .setStartDelay(delay)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }

    private fun animatePop(view: View, delay: Long) {
        view.alpha = 0f
        view.scaleX = 0.85f
        view.scaleY = 0.85f

        view.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(400)
            .setStartDelay(delay)
            .setInterpolator(OvershootInterpolator())
            .start()
    }

    // ------------------------------------------------------------
    private fun setupDoctorInfoFromIntent() {
        doctorId = intent.getStringExtra("doctorId") ?: ""
        doctorName = intent.getStringExtra("doctorName") ?: ""

        if (doctorId.isEmpty()) {
            SuperToast.show(this, SuperToast.Type.ERROR, "Missing", "Doctor info missing!")
            finish()
            return
        }

        binding.tvDoctorName.text = doctorName
    }

    // ------------------------------------------------------------
    private fun fetchDoctorFullDetails() {
        db.child("doctors").child(doctorId).get()
            .addOnSuccessListener { snap ->
                if (!snap.exists()) return@addOnSuccessListener

                val imageUrl = snap.child("imageUrl").value?.toString() ?: ""
                if (imageUrl.isNotEmpty()) {
                    Glide.with(this)
                        .load(imageUrl)
                        .centerCrop()
                        .into(binding.ivDoctorPhoto)
                }

                val specialization = snap.child("specialization").value?.toString() ?: "Not available"
                val exp = snap.child("experience").value?.toString() ?: "0"

                binding.tvSpecialization.text = specialization
                binding.tvExperience.text = "$exp years experience"

                binding.tvAboutDoctor.text =
                    snap.child("about").value?.toString() ?: "No details available."

                val clinic = snap.child("clinicName").value?.toString() ?: ""
                val fee = snap.child("consultationFee").value?.toString() ?: ""

                binding.tvClinicInfo.text = "$clinic\nConsultation Fee: ₹$fee"

                val weekList = mutableListOf<String>()
                snap.child("weeklyAvailability").children.forEach {
                    weekList.add(it.value.toString())
                }
                showWeekAvailability(weekList)

                val ach = snap.child("achievements").value?.toString() ?: ""
                showAchievements(ach)

                val slotCount = (snap.child("availableSlots").value?.toString() ?: "0").toInt()
                showSlotChips(slotCount)

            }.addOnFailureListener {
                Log.e("BookAppointment", "Error: ${it.message}")
            }
    }

    // ------------------------------------------------------------
    private fun showWeekAvailability(days: List<String>) {
        binding.layoutWeekAvailability.removeAllViews()

        for (day in days) {
            val tv = TextView(this).apply {
                text = "• $day"
                textSize = 15f
                setPadding(10, 10, 10, 10)
                setTextColor(resources.getColor(android.R.color.black))
            }
            binding.layoutWeekAvailability.addView(tv)
        }
    }

    // ------------------------------------------------------------
    private fun showAchievements(achievementText: String) {
        binding.layoutAwards.removeAllViews()

        val finalText = if (achievementText.isEmpty())
            "No achievements added"
        else achievementText

        val tv = TextView(this).apply {
            setPadding(14, 12, 14, 12)
            textSize = 15f
            setTextColor(resources.getColor(android.R.color.black))
            text = finalText
        }

        binding.layoutAwards.addView(tv)
    }

    // ------------------------------------------------------------
    private fun showSlotChips(count: Int) {
        binding.chipSlots.removeAllViews()

        for (i in 1..count) {
            val chip = Chip(this).apply {
                text = "Slot $i"
                isCheckable = true
            }
            binding.chipSlots.addView(chip)
        }
    }

    // ------------------------------------------------------------
    private fun setupDatePicker() {
        binding.etDate.setOnClickListener {
            val c = Calendar.getInstance()

            DatePickerDialog(
                this,
                { _, y, m, d ->
                    binding.etDate.setText(String.format("%02d/%02d/%04d", d, m + 1, y))
                },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            ).apply {
                datePicker.minDate = System.currentTimeMillis()
            }.show()
        }
    }

    // ------------------------------------------------------------
    private fun setupTimePicker() {
        binding.etTime.setOnClickListener {
            val c = Calendar.getInstance()
            TimePickerDialog(
                this,
                { _, h, mi -> binding.etTime.setText(String.format("%02d:%02d", h, mi)) },
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                true
            ).show()
        }
    }

    // ------------------------------------------------------------
    private fun setupBookButton() {
        binding.btnBook.setOnClickListener {
            // Button animation
            binding.btnBook.animate()
                .scaleX(0.93f)
                .scaleY(0.93f)
                .setDuration(120)
                .withEndAction {
                    binding.btnBook.animate().scaleX(1f).scaleY(1f).duration = 120
                }.start()

            val date = binding.etDate.text.toString()
            val time = binding.etTime.text.toString()

            if (date.isEmpty() || time.isEmpty()) {
                SuperToast.show(this, SuperToast.Type.WARNING, "Missing Info", "Select both date & time")
                return@setOnClickListener
            }

            val selectedChipId = binding.chipSlots.checkedChipId
            if (selectedChipId == -1) {
                SuperToast.show(this, SuperToast.Type.WARNING, "Slot Required", "Please select a slot")
                return@setOnClickListener
            }

            val slotText = findViewById<Chip>(selectedChipId).text.toString()
            val slotNumber = slotText.replace("Slot ", "").toIntOrNull() ?: 1
            val userId = auth.currentUser?.uid ?: return@setOnClickListener

            // Check existing bookings for token number
            db.child("appointments").orderByChild("doctorId").equalTo(doctorId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var sameDateCount = 0
                        for (child in snapshot.children) {
                            val dbDate = child.child("date").value?.toString() ?: ""
                            if (dbDate == date) {
                                sameDateCount++
                            }
                        }

                        val newToken = String.format("%02d", sameDateCount + 1)
                        saveAppointment(userId, date, time, slotText, newToken, slotNumber)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        SuperToast.show(
                            this@BookAppointmentActivity,
                            SuperToast.Type.ERROR,
                            "Error",
                            error.message
                        )
                    }
                })
        }
    }

    // ------------------------------------------------------------
    private fun saveAppointment(
        userId: String,
        date: String,
        time: String,
        slot: String,
        token: String,
        slotNumber: Int
    ) {
        val appId = db.child("appointments").push().key!!

        db.child("users").child(userId).get().addOnSuccessListener { uSnap ->
            val userName = uSnap.child("name").value?.toString() ?: "User"

            val appointmentMap = hashMapOf(
                "appointmentId" to appId,
                "userId" to userId,
                "userName" to userName,
                "doctorId" to doctorId,
                "doctorName" to doctorName,
                "date" to date,
                "time" to time,
                "slot" to slot,
                "tokenNumber" to token,
                "status" to "Pending"
            )

            val updates = hashMapOf<String, Any>(
                "/appointments/$appId" to appointmentMap,
                "/users/$userId/myAppointments/$appId" to appointmentMap,
                "/doctors/$doctorId/requests/$appId" to appointmentMap
            )

            db.updateChildren(updates)
                .addOnSuccessListener {
                    Log.d("BookAppointment", "✅ Booking saved: $appId")

                    // 🎉 Show confirmation dialog
                    showBookingConfirmationDialog(
                        doctorName = doctorName,
                        date = date,
                        time = time,
                        slot = slotNumber,
                        token = token,
                        refId = appId
                    )

                }
                .addOnFailureListener { e ->
                    Log.e("BookAppointment", "❌ Booking failed: ${e.message}")
                    SuperToast.show(
                        this@BookAppointmentActivity,
                        SuperToast.Type.ERROR,
                        "Error",
                        "Failed to book appointment"
                    )
                }
        }
    }

    // 🎉 SHOW BOOKING CONFIRMATION DIALOG
    private fun showBookingConfirmationDialog(
        doctorName: String,
        date: String,
        time: String,
        slot: Int,
        token: String,
        refId: String
    ) {
        // Convert date string to Date object
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val appointmentDate = try {
            dateFormat.parse(date)
        } catch (e: Exception) {
            Date()
        }

        // Create and show dialog
        val dialog = BookingConfirmDialog.newInstance(
            doctorName = doctorName,
            date = appointmentDate ?: Date(),
            time = time,
            slot = slot,
            tokenNumber = token,
            refId = refId
        )

        dialog.setOnDismissListener {
            finish()
        }

        dialog.show(supportFragmentManager, "BookingConfirmDialog")
    }
}