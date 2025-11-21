package com.example.bookmyhealth.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.animation.AlphaAnimation
import androidx.appcompat.app.AppCompatActivity
import com.example.bookmyhealth.databinding.ActivitySplashBinding
import com.example.bookmyhealth.ui.auth.RoleSelectActivity
import com.example.bookmyhealth.ui.auth.doctor.DoctorDashboardActivity
import com.example.bookmyhealth.ui.auth.user.UserDashboardActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val auth by lazy { FirebaseAuth.getInstance() }

    // ✅ Use correct regional Firebase URL
    private val dbRef by lazy {
        FirebaseDatabase.getInstance(
            "https://bookmyhealth-5920a-default-rtdb.asia-southeast1.firebasedatabase.app"
        ).getReference("users")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startIntroAnimation()

        // 🕒 Delay splash for smoother fade
        binding.root.postDelayed({
            checkLoginStatus()
        }, 2000)
    }

    /** ✨ Simple fade animations */
    private fun startIntroAnimation() {
        val fadeLogo = AlphaAnimation(0f, 1f).apply {
            duration = 1000
            fillAfter = true
        }
        val fadeAppName = AlphaAnimation(0f, 1f).apply {
            duration = 800
            startOffset = 600
            fillAfter = true
        }
        val fadeTagline = AlphaAnimation(0f, 1f).apply {
            duration = 800
            startOffset = 1000
            fillAfter = true
        }

        binding.ivLogo.startAnimation(fadeLogo)
        binding.tvAppName.startAnimation(fadeAppName)
        binding.tvTagline.startAnimation(fadeTagline)
    }

    /** 🔍 Check FirebaseAuth & user role */
    private fun checkLoginStatus() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            navigateTo(RoleSelectActivity::class.java)
            return
        }

        dbRef.child(currentUser.uid).child("role").get()
            .addOnSuccessListener { snapshot ->
                val role = snapshot.value?.toString()?.lowercase()
                when (role) {
                    "doctor" -> navigateTo(DoctorDashboardActivity::class.java)
                    "user" -> navigateTo(UserDashboardActivity::class.java)
                    else -> navigateTo(RoleSelectActivity::class.java)
                }
            }
            .addOnFailureListener {
                navigateTo(RoleSelectActivity::class.java)
            }
    }

    /** 🚀 Smooth transition navigation */
    private fun navigateTo(destination: Class<*>) {
        val intent = Intent(this, destination).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}
