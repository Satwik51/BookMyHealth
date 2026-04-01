package com.example.bookmyhealth.ui.profile

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.TextView
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
import java.io.ByteArrayOutputStream

class DoctorProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorProfileBinding
    private val auth by lazy { FirebaseAuth.getInstance() }
    private lateinit var dbRef: DatabaseReference
    private lateinit var uid: String
    private var valueEventListener: ValueEventListener? = null

    private var selectedImageUri: Uri? = null
    private var selectedCertUri: Uri? = null
    private var progressDialog: ProgressDialog? = null

    private var existingImageUrl: String = ""
    private var existingCertUrl: String = ""

    // 🖼️ Profile Image Picker
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedImageUri = result.data?.data
            Glide.with(this).load(selectedImageUri).circleCrop().into(binding.ivProfile)
            SuperToast.show(this, SuperToast.Type.SUCCESS, "Image Selected", "Profile image updated")
        }
    }

    // 📜 Certificate Picker
    private val pickCertLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedCertUri = result.data?.data
            binding.cardCertificatePreview.visibility = View.VISIBLE
            Glide.with(this).load(selectedCertUri).into(binding.ivCertificatePreview)
            SuperToast.show(this, SuperToast.Type.SUCCESS, "Certificate Selected", "Ready to update")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        uid = auth.currentUser?.uid ?: ""
        if (uid.isEmpty()) { finish(); return }

        dbRef = FirebaseDatabase.getInstance("https://bookmyhealth-5920a-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("doctors").child(uid)

        setupSpecializationDropdown()
        setupAvailabilityChips()
        setupRealtimeProfile()
        setupUpdateButton()
        setupLogoutButton()
        setupFullscreenLogic() // 🟢 Naya Preview Logic

        // Listeners for Upload Buttons
        binding.ivProfile.setOnClickListener {
            val pick = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
            pickImageLauncher.launch(pick)
        }

        binding.btnUploadCertificate.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
            pickCertLauncher.launch(intent)
        }
    }

    // 🟢 Fullscreen Preview Logic
    private fun setupFullscreenLogic() {
        // Choti image click -> Fullscreen open
        binding.ivCertificatePreview.setOnClickListener {
            val drawable = binding.ivCertificatePreview.drawable
            if (drawable != null) {
                binding.ivFullscreenImage.setImageDrawable(drawable)
                binding.layoutFullscreenPreview.visibility = View.VISIBLE
            }
        }

        // Close button click -> Fullscreen hide
        binding.btnClosePreview.setOnClickListener {
            binding.layoutFullscreenPreview.visibility = View.GONE
        }

        // Background click -> Fullscreen hide
        binding.layoutFullscreenPreview.setOnClickListener {
            binding.layoutFullscreenPreview.visibility = View.GONE
        }
    }

    private fun setupRealtimeProfile() {
        valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val doctor = snapshot.getValue(Doctor::class.java) ?: return

                existingImageUrl = doctor.imageUrl ?: ""
                existingCertUrl = doctor.certificateUrl ?: ""

                binding.etName.text = "Dr. ${doctor.name}"
                binding.etSpecialization.setText(doctor.specialization)
                binding.etExperience.setText(doctor.experience)
                binding.etAvailableSlots.setText(doctor.availableSlots)
                binding.tvEmail.text = doctor.email
                binding.tvAbout.setText(doctor.about)
                binding.etClinicName.setText(doctor.clinicName)
                binding.etConsultationFee.setText(doctor.consultationFee)
                binding.etAchievements.setText(doctor.achievements)

                if (selectedImageUri == null && existingImageUrl.isNotEmpty()) {
                    loadBase64Image(existingImageUrl, binding.ivProfile, true)
                }

                if (selectedCertUri == null && existingCertUrl.isNotEmpty()) {
                    binding.cardCertificatePreview.visibility = View.VISIBLE
                    loadBase64Image(existingCertUrl, binding.ivCertificatePreview, false)
                }

                doctor.weeklyAvailability?.let { restoreSelectedChips(it) }
            }
            override fun onCancelled(error: DatabaseError) {}
        }
        dbRef.addValueEventListener(valueEventListener!!)
    }

    private fun setupUpdateButton() {
        binding.btnUpdateProfile.setOnClickListener {
            showProgress("Updating Profile...")

            Thread {
                val availability = mutableListOf<String>()
                val chips = mapOf(
                    "Mon" to binding.monChip, "Tue" to binding.tueChip,
                    "Wed" to binding.wedChip, "Thu" to binding.thuChip,
                    "Fri" to binding.friChip, "Sat" to binding.satChip, "Sun" to binding.sunChip
                )
                chips.forEach { (day, chip) -> if (chip.isSelected) availability.add(day) }

                val finalImgBase64 = if (selectedImageUri != null) uriToBase64(selectedImageUri!!) else existingImageUrl
                val finalCertBase64 = if (selectedCertUri != null) uriToBase64(selectedCertUri!!) else existingCertUrl

                runOnUiThread {
                    saveToFirebase(finalImgBase64 ?: "", finalCertBase64 ?: "", availability)
                }
            }.start()
        }
    }

    private fun saveToFirebase(img: String, cert: String, days: List<String>) {
        val updateMap = mapOf(
            "specialization" to binding.etSpecialization.text.toString(),
            "experience" to binding.etExperience.text.toString(),
            "availableSlots" to binding.etAvailableSlots.text.toString(),
            "about" to binding.tvAbout.text.toString(),
            "clinicName" to binding.etClinicName.text.toString(),
            "consultationFee" to binding.etConsultationFee.text.toString(),
            "achievements" to binding.etAchievements.text.toString(),
            "weeklyAvailability" to days,
            "imageUrl" to img,
            "certificateUrl" to cert
        )

        dbRef.updateChildren(updateMap).addOnSuccessListener {
            hideProgress()
            selectedImageUri = null
            selectedCertUri = null
            SuperToast.show(this, SuperToast.Type.SUCCESS, "Updated", "Doctor profile is now current")
        }.addOnFailureListener {
            hideProgress()
            SuperToast.show(this, SuperToast.Type.ERROR, "Failed", it.message.toString())
        }
    }

    private fun loadBase64Image(base64Str: String, imageView: android.widget.ImageView, isCircle: Boolean) {
        try {
            val imageBytes = Base64.decode(base64Str, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

            // Glide request create karein
            val request = Glide.with(this).asBitmap().load(bitmap)

            // Agar circle dikhana hai (Profile Image ke liye)
            if (isCircle) {
                request.circleCrop()
            } else {
                // 🟢 YAHAN CHANGE HAI:
                // Certificate ya normal images ke liye koi crop modifier mat lagayein.
                // FitCenter use karein taaki image scale ho par crop na ho.
                request.fitCenter()
            }

            // Final image set karein
            request.into(imageView)

        } catch (e: Exception) {
            // Error ke case mein placeholder
            imageView.setImageResource(R.drawable.placeholder_profile)
        }
    }

    private fun uriToBase64(uri: Uri): String? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, outputStream)
            Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
        } catch (e: Exception) { null }
    }

    private fun setupSpecializationDropdown() {
        val list = listOf("Dentist", "Cardiologist", "Neurologist", "Orthopedic", "General Physician", "Pediatrician")
        val adapter = android.widget.ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, list)
        binding.etSpecialization.setAdapter(adapter)
    }

    private fun setupAvailabilityChips() {
        val chips = listOf(binding.monChip, binding.tueChip, binding.wedChip, binding.thuChip, binding.friChip, binding.satChip, binding.sunChip)
        chips.forEach { chip ->
            chip.setOnClickListener {
                chip.isSelected = !chip.isSelected
                updateChipUI(chip, chip.isSelected)
            }
        }
    }

    private fun updateChipUI(chip: TextView, selected: Boolean) {
        if (selected) {
            chip.setBackgroundResource(R.drawable.bg_chip_selected)
            chip.setTextColor(getColor(R.color.white))
        } else {
            chip.setBackgroundResource(R.drawable.bg_chip)
            chip.setTextColor(getColor(R.color.darkPrimaryText))
        }
    }

    private fun restoreSelectedChips(days: List<String>) {
        val map = mapOf("Mon" to binding.monChip, "Tue" to binding.tueChip, "Wed" to binding.wedChip, "Thu" to binding.thuChip, "Fri" to binding.friChip, "Sat" to binding.satChip, "Sun" to binding.sunChip)
        map.values.forEach {
            it.isSelected = false
            updateChipUI(it, false)
        }
        days.forEach { day -> map[day]?.let { it.isSelected = true; updateChipUI(it, true) } }
    }

    private fun setupLogoutButton() {
        binding.btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, RoleSelectActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }
    }

    private fun showProgress(msg: String) {
        progressDialog = ProgressDialog(this).apply { setMessage(msg); setCancelable(false); show() }
    }

    private fun hideProgress() { progressDialog?.dismiss() }

    // 🟢 Back Button handle agar preview khula ho
    override fun onBackPressed() {
        if (binding.layoutFullscreenPreview.visibility == View.VISIBLE) {
            binding.layoutFullscreenPreview.visibility = View.GONE
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        valueEventListener?.let { dbRef.removeEventListener(it) }
    }
}