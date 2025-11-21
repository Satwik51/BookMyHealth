package com.example.bookmyhealth.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bookmyhealth.databinding.ActivityLoadingBinding
import com.example.bookmyhealth.ui.auth.doctor.DoctorLoginActivity
import com.example.bookmyhealth.ui.auth.user.UserLoginActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoadingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 🔹 Get role passed from RoleSelectActivity
        val role = intent.getStringExtra("role")

        // 🔹 Start loading animation
        binding.lottieLoading.playAnimation()

        // 🔹 Use coroutine for smooth delay (modern alternative to Handler)
        lifecycleScope.launch {
            delay(2500) // 2.5 seconds delay for loading animation
            when (role) {
                "user" -> startActivity(Intent(this@LoadingActivity, UserLoginActivity::class.java))
                "doctor" -> startActivity(Intent(this@LoadingActivity, DoctorLoginActivity::class.java))
                else -> finish() // fallback (in case role is missing)
            }

            // 🔹 Smooth transition effect
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

            // 🔹 Finish to prevent going back
            finish()
        }
    }
}
