package com.example.bookmyhealth.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bookmyhealth.R
import com.example.bookmyhealth.data.model.Doctor
import com.example.bookmyhealth.databinding.ActivityDoctorProfileBinding
import com.example.bookmyhealth.ui.auth.RoleSelectActivity
import com.example.bookmyhealth.utils.SuperToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class DoctorProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorProfileBinding
    private val auth by lazy { FirebaseAuth.getInstance() }
    private lateinit var dbRef: DatabaseReference
    private lateinit var uid: String
    private lateinit var valueEventListener: ValueEventListener
    private var selectedImageUri: Uri? = null

    private val storageRef by lazy {
        FirebaseStorage.getInstance().reference.child("doctor_profiles")
    }

    // ---------------- IMAGE PICKER ----------------
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                selectedImageUri = result.data?.data
                binding.ivProfile.setImageURI(selectedImageUri)

                binding.ivProfile.animate()
                    .scaleX(1.05f).scaleY(1.05f)
                    .setDuration(200)
                    .withEndAction {
                        binding.ivProfile.animate().scaleX(1f).scaleY(1f).duration = 150
                    }.start()

                SuperToast.show(
                    this,
                    SuperToast.Type.SUCCESS,
                    "Image Selected",
                    "Profile image loaded"
                )
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        uid = auth.currentUser?.uid ?: ""

        if (uid.isEmpty()) {
            SuperToast.show(this, SuperToast.Type.ERROR, "Error", "Doctor not logged in!")
            finish()
            return
        }

        dbRef = FirebaseDatabase.getInstance(
            "https://bookmyhealth-5920a-default-rtdb.asia-southeast1.firebasedatabase.app"
        ).getReference("doctors").child(uid)

        binding.tvEmail.text = auth.currentUser?.email ?: ""

        setupSpecializationDropdown()
        setupAvailabilityChips()
        setupRealtimeProfile()
        setupUpdateButton()
        setupLogoutButton()
        setupImageUpload()
        setupCertificateUpload()

        runEntryAnimations()
    }

    // ---------------------------------------------------------
    // ENTRY ANIMATIONS
    // ---------------------------------------------------------
    private fun runEntryAnimations() {

        // PROFILE IMAGE
        binding.ivProfile.apply {
            alpha = 0f
            scaleX = 0.7f
            scaleY = 0.7f
            animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(550)
                .setInterpolator(OvershootInterpolator())
                .start()
        }

        binding.etName.apply {
            translationX = -80f
            alpha = 0f
            animate()
                .translationX(0f)
                .alpha(1f)
                .setDuration(450)
                .setStartDelay(120)
                .start()
        }

        val fields = listOf(
            binding.etSpecialization,
            binding.etExperience,
            binding.etAvailableSlots,
            binding.etClinicName,
            binding.etConsultationFee,
            binding.etAchievements,
            binding.tvAbout
        )

        fields.forEachIndexed { index, view ->
            view.alpha = 0f
            view.translationY = 40f
            view.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(450)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .setStartDelay(150L * (index + 1))
                .start()
        }

        binding.btnUpdateProfile.apply {
            scaleX = 0.8f
            scaleY = 0.8f
            alpha = 0f
            animate()
                .scaleX(1f).scaleY(1f).alpha(1f)
                .setDuration(500)
                .setStartDelay(800)
                .setInterpolator(OvershootInterpolator())
                .start()
        }

        binding.btnLogout.apply {
            alpha = 0f
            translationY = 30f
            animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(450)
                .setStartDelay(950)
                .start()
        }

        val chips = listOf(
            binding.monChip, binding.tueChip, binding.wedChip,
            binding.thuChip, binding.friChip, binding.satChip, binding.sunChip
        )

        chips.forEachIndexed { i, chip ->
            chip.scaleX = 0f
            chip.scaleY = 0f
            chip.animate()
                .scaleX(1f).scaleY(1f)
                .setDuration(350)
                .setInterpolator(OvershootInterpolator())
                .setStartDelay(300L + i * 100)
                .start()
        }
    }

    // ---------------------------------------------------------
    private fun setupSpecializationDropdown() {
        val specializationList = listOf(
            "General Physician", "Cardiologist", "Dermatologist", "Neurologist", "Psychiatrist",
            "Dentist", "Orthopedic Surgeon", "Gynecologist", "Pediatrician", "Ophthalmologist",
            "ENT Specialist", "Urologist", "Nephrologist", "Oncologist", "Endocrinologist"
        )

        val adapter = android.widget.ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            specializationList
        )

        binding.etSpecialization.setAdapter(adapter)
        binding.etSpecialization.setOnClickListener { binding.etSpecialization.showDropDown() }
    }

    // ---------------------------------------------------------
    private fun setupAvailabilityChips() {
        val chips = listOf(
            binding.monChip, binding.tueChip, binding.wedChip,
            binding.thuChip, binding.friChip, binding.satChip, binding.sunChip
        )

        chips.forEach { chip ->
            chip.isSelected = false
            updateChipUI(chip, false)

            chip.setOnClickListener {
                val newState = !chip.isSelected
                chip.isSelected = newState
                updateChipUI(chip, newState)

                chip.animate()
                    .scaleX(1.1f).scaleY(1.1f)
                    .setDuration(120)
                    .withEndAction {
                        chip.animate().scaleX(1f).scaleY(1f).duration = 120
                    }.start()
            }
        }
    }

    private fun updateChipUI(chip: TextView, selected: Boolean) {
        if (selected) {
            chip.setBackgroundResource(R.drawable.bg_chip_selected)
            chip.setTextColor(getColor(R.color.white))
            chip.alpha = 1f
        } else {
            chip.setBackgroundResource(R.drawable.bg_chip)
            chip.setTextColor(getColor(R.color.darkPrimaryText))
            chip.alpha = 0.6f
        }
    }

    // ---------------------------------------------------------
    private fun setupImageUpload() {
        binding.ivProfile.setOnClickListener {
            val pick = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
            pickImageLauncher.launch(pick)
        }
    }

    private fun setupCertificateUpload() {
        binding.btnUploadCertificate.setOnClickListener {
            SuperToast.show(
                this,
                SuperToast.Type.WARNING,
                "Coming Soon",
                "Certificate upload coming soon!"
            )
        }
    }

    // ---------------------------------------------------------
    private fun setupRealtimeProfile() {

        valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val doctor = snapshot.getValue(Doctor::class.java) ?: return

                binding.etName.setText(doctor.name)
                binding.etSpecialization.setText(doctor.specialization)
                binding.etExperience.setText(doctor.experience)
                binding.etAvailableSlots.setText(doctor.availableSlots)
                binding.tvEmail.text = doctor.email
                binding.tvAbout.setText(doctor.about)
                binding.etClinicName.setText(doctor.clinicName)
                binding.etConsultationFee.setText(doctor.consultationFee)
                binding.etAchievements.setText(doctor.achievements)

                Glide.with(this@DoctorProfileActivity)
                    .load(doctor.imageUrl)
                    .placeholder(R.drawable.profile_placeholder)
                    .circleCrop()
                    .into(binding.ivProfile)

                doctor.weeklyAvailability?.let { restoreSelectedChips(it) }
            }

            override fun onCancelled(error: DatabaseError) {}
        }

        dbRef.addValueEventListener(valueEventListener)
    }

    private fun restoreSelectedChips(days: List<String>) {
        val map = mapOf(
            "Mon" to binding.monChip,
            "Tue" to binding.tueChip,
            "Wed" to binding.wedChip,
            "Thu" to binding.thuChip,
            "Fri" to binding.friChip,
            "Sat" to binding.satChip,
            "Sun" to binding.sunChip
        )

        map.values.forEach {
            it.isSelected = false
            updateChipUI(it, false)
        }

        days.forEach { day ->
            map[day]?.let {
                it.isSelected = true
                updateChipUI(it, true)
            }
        }
    }

    // ---------------------------------------------------------
    private fun setupUpdateButton() {
        binding.btnUpdateProfile.setOnClickListener {

            binding.btnUpdateProfile.animate()
                .scaleX(0.92f).scaleY(0.92f)
                .setDuration(120)
                .withEndAction {
                    binding.btnUpdateProfile.animate().scaleX(1f).scaleY(1f).duration = 120
                }.start()

            val name = binding.etName.text.toString().trim()
            val specialization = binding.etSpecialization.text.toString().trim()
            val experience = binding.etExperience.text.toString().trim()
            val slots = binding.etAvailableSlots.text.toString().trim()

            if (name.isEmpty() || specialization.isEmpty() || experience.isEmpty() || slots.isEmpty()) {

                SuperToast.show(
                    this,
                    SuperToast.Type.ERROR,
                    "Form Incomplete",
                    "Fill all required fields!"
                )
                return@setOnClickListener
            }

            val avail = buildList {
                if (binding.monChip.isSelected) add("Mon")
                if (binding.tueChip.isSelected) add("Tue")
                if (binding.wedChip.isSelected) add("Wed")
                if (binding.thuChip.isSelected) add("Thu")
                if (binding.friChip.isSelected) add("Fri")
                if (binding.satChip.isSelected) add("Sat")
                if (binding.sunChip.isSelected) add("Sun")
            }

            if (selectedImageUri != null) {
                uploadImageAndSaveData(selectedImageUri!!, avail)
            } else {
                dbRef.child("imageUrl").get().addOnSuccessListener {
                    saveDoctorData(it.value?.toString(), avail)
                }
            }
        }
    }

    private fun uploadImageAndSaveData(uri: Uri, availability: List<String>) {
        val ref = storageRef.child("$uid.jpg")

        ref.putFile(uri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { link ->
                    saveDoctorData(link.toString(), availability)
                }
            }
            .addOnFailureListener {
                SuperToast.show(
                    this,
                    SuperToast.Type.ERROR,
                    "Upload Failed",
                    "Image upload failed!"
                )
            }
    }

    private fun saveDoctorData(imageUrl: String?, availability: List<String>) {
        val updatedDoctor = Doctor(
            uid = uid,
            name = binding.etName.text.toString(),
            email = binding.tvEmail.text.toString(),
            specialization = binding.etSpecialization.text.toString(),
            experience = binding.etExperience.text.toString(),
            availableSlots = binding.etAvailableSlots.text.toString(),
            about = binding.tvAbout.text.toString(),
            clinicName = binding.etClinicName.text.toString(),
            consultationFee = binding.etConsultationFee.text.toString(),
            achievements = binding.etAchievements.text.toString(),
            weeklyAvailability = availability,
            imageUrl = imageUrl ?: "",
            role = "Doctor"
        )

        dbRef.setValue(updatedDoctor)
            .addOnSuccessListener {
                SuperToast.show(
                    this,
                    SuperToast.Type.SUCCESS,
                    "Success",
                    "Profile Updated Successfully!"
                )
            }
            .addOnFailureListener {
                SuperToast.show(
                    this,
                    SuperToast.Type.ERROR,
                    "Update Failed",
                    "Update failed!"
                )
            }
    }

    // ---------------------------------------------------------
    private fun setupLogoutButton() {
        binding.btnLogout.setOnClickListener {
            auth.signOut()

            SuperToast.show(
                this,
                SuperToast.Type.WARNING,
                "Logged Out",
                "You have been logged out"
            )

            startActivity(
                Intent(this, RoleSelectActivity::class.java)
                    .apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
            )
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try { dbRef.removeEventListener(valueEventListener) } catch (_: Exception) {}
    }
}
