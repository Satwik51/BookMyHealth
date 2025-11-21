package com.example.bookmyhealth.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bookmyhealth.R
import com.example.bookmyhealth.data.model.User
import com.example.bookmyhealth.databinding.ActivityUserProfileBinding
import com.example.bookmyhealth.ui.auth.RoleSelectActivity
import com.example.bookmyhealth.utils.SuperToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding
    private val auth by lazy { FirebaseAuth.getInstance() }
    private lateinit var dbRef: DatabaseReference
    private lateinit var uid: String
    private lateinit var listener: ValueEventListener

    private val storageRef by lazy {
        FirebaseStorage.getInstance().reference.child("user_profiles")
    }

    private var selectedImageUri: Uri? = null

    // Image Picker
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                selectedImageUri = result.data?.data
                binding.ivProfile.setImageURI(selectedImageUri)

                binding.ivProfile.animate()
                    .scaleX(1.07f).scaleY(1.07f)
                    .setDuration(120)
                    .withEndAction {
                        binding.ivProfile.animate().scaleX(1f).scaleY(1f).duration = 120
                    }.start()

                SuperToast.show(
                    this,
                    SuperToast.Type.SUCCESS,
                    "Image Selected",
                    "New profile image loaded"
                )
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        uid = auth.currentUser?.uid ?: ""
        if (uid.isEmpty()) {
            SuperToast.show(this, SuperToast.Type.ERROR, "Error", "User not logged in")
            finish()
            return
        }

        dbRef = FirebaseDatabase.getInstance(
            "https://bookmyhealth-5920a-default-rtdb.asia-southeast1.firebasedatabase.app"
        ).getReference("users").child(uid)

        binding.tvEmail.text = auth.currentUser?.email ?: ""

        setupRealtimeProfile()
        setupImagePicker()
        setupUpdateButton()
        setupLogoutButton()

        runEntryAnimations()
    }

    // ---------------------------------------------------------------
    // ⭐ ANIMATIONS
    // ---------------------------------------------------------------
    private fun runEntryAnimations() {

        binding.ivProfile.apply {
            alpha = 0f
            scaleX = 0.7f
            scaleY = 0.7f

            animate()
                .alpha(1f)
                .scaleX(1f).scaleY(1f)
                .setDuration(550)
                .setInterpolator(OvershootInterpolator())
                .start()
        }

        animateSlideUp(binding.tvName, 100)
        animateSlideUp(binding.tvEmail, 150)
        animateSlideUp(binding.etPhone, 200)
        animateSlideUp(binding.etAge, 250)
        animateSlideUp(binding.etAddress, 300)

        binding.btnUpdateProfile.apply {
            alpha = 0f
            translationY = 60f
            animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(500)
                .setStartDelay(350)
                .setInterpolator(OvershootInterpolator())
                .start()
        }

        binding.btnLogout.apply {
            alpha = 0f
            translationY = 50f
            animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(450)
                .setStartDelay(450)
                .start()
        }
    }

    private fun animateSlideUp(view: View, delay: Long) {
        view.alpha = 0f
        view.translationY = 40f

        view.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(450)
            .setStartDelay(delay)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }

    // -------------------------------------------------------------------
    private fun setupImagePicker() {
        binding.ivProfile.setOnClickListener {

            binding.ivProfile.animate()
                .scaleX(0.93f).scaleY(0.93f)
                .setDuration(100)
                .withEndAction {
                    binding.ivProfile.animate().scaleX(1f).scaleY(1f).duration = 120
                }.start()

            Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
                pickImageLauncher.launch(this)
            }
        }
    }

    // -------------------------------------------------------------------
    private fun setupRealtimeProfile() {
        listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val user = snapshot.getValue(User::class.java)

                user?.let {
                    binding.tvName.text = it.name
                    binding.etPhone.setText(it.phone)
                    binding.etAge.setText(it.age)
                    binding.etAddress.setText(it.address)
                    binding.tvEmail.text = it.email
                }

                val firebaseImage = user?.imageUrl ?: ""
                val googleImage = auth.currentUser?.photoUrl?.toString()

                val finalImage = when {
                    firebaseImage.isNotEmpty() -> firebaseImage
                    googleImage != null -> googleImage
                    else -> null
                }

                Glide.with(this@UserProfileActivity)
                    .load(finalImage)
                    .placeholder(R.drawable.profile_placeholder)
                    .circleCrop()
                    .into(binding.ivProfile)
            }

            override fun onCancelled(error: DatabaseError) {
                SuperToast.show(
                    this@UserProfileActivity,
                    SuperToast.Type.ERROR,
                    "Failed",
                    error.message
                )
            }
        }

        dbRef.addValueEventListener(listener)
    }

    // -------------------------------------------------------------------
    private fun setupUpdateButton() {
        binding.btnUpdateProfile.setOnClickListener {

            binding.btnUpdateProfile.animate()
                .scaleX(0.92f).scaleY(0.92f)
                .setDuration(120)
                .withEndAction {
                    binding.btnUpdateProfile.animate().scaleX(1f).scaleY(1f).duration = 120
                }.start()

            val name = binding.tvName.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            val age = binding.etAge.text.toString().trim()
            val address = binding.etAddress.text.toString().trim()

            if (name.isEmpty() || phone.isEmpty()) {
                SuperToast.show(
                    this,
                    SuperToast.Type.WARNING,
                    "Missing Info",
                    "Name & phone required!"
                )
                return@setOnClickListener
            }

            if (selectedImageUri != null) {
                uploadImageAndSave(name, phone, age, address)
            } else {
                saveProfile("", name, phone, age, address)
            }
        }
    }

    // -------------------------------------------------------------------
    private fun uploadImageAndSave(name: String, phone: String, age: String, address: String) {
        val ref = storageRef.child("$uid.jpg")

        ref.putFile(selectedImageUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { link ->
                    saveProfile(link.toString(), name, phone, age, address)
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

    // -------------------------------------------------------------------
    private fun saveProfile(
        imageUrl: String,
        name: String,
        phone: String,
        age: String,
        address: String
    ) {
        val finalImage =
            if (imageUrl.isNotEmpty()) imageUrl
            else auth.currentUser?.photoUrl?.toString() ?: ""

        val updatedUser = User(
            uid = uid,
            name = name,
            email = binding.tvEmail.text.toString(),
            phone = phone,
            age = age,
            address = address,
            imageUrl = finalImage,
            role = "User"
        )

        dbRef.setValue(updatedUser)
            .addOnSuccessListener {
                SuperToast.show(
                    this,
                    SuperToast.Type.SUCCESS,
                    "Updated",
                    "Profile updated successfully!"
                )
            }
            .addOnFailureListener {
                SuperToast.show(
                    this,
                    SuperToast.Type.ERROR,
                    "Failed",
                    "Failed to update profile!"
                )
            }
    }

    // -------------------------------------------------------------------
    private fun setupLogoutButton() {
        binding.btnLogout.setOnClickListener {

            binding.btnLogout.animate()
                .scaleX(0.92f).scaleY(0.92f)
                .setDuration(120)
                .withEndAction {
                    binding.btnLogout.animate().scaleX(1f).scaleY(1f).duration = 120
                }.start()

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
        dbRef.removeEventListener(listener)
    }
}
