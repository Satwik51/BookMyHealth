package com.example.bookmyhealth.ui.auth.doctor

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bookmyhealth.R
import com.example.bookmyhealth.databinding.ActivityDoctorLoginBinding
import com.example.bookmyhealth.ui.auth.RoleSelectActivity
import com.example.bookmyhealth.utils.SuperToast
import com.example.bookmyhealth.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class DoctorLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorLoginBinding
    private val viewModel: AuthViewModel by viewModels()

    // ---------------------------------------------------------
    // GOOGLE SIGN-IN HANDLER
    // ---------------------------------------------------------
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            val account = task.getResult(ApiException::class.java)

            val idToken = account?.idToken
            if (idToken != null) {

                showLoading(true)

                viewModel.loginWithGoogle(idToken, "doctor") { success, message ->
                    showLoading(false)

                    if (success) {
                        SuperToast.show(
                            this,
                            SuperToast.Type.SUCCESS,
                            "Login Successful",
                            message
                        )

                        val intent = Intent(this, DoctorDashboardActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()

                    } else {
                        SuperToast.show(
                            this,
                            SuperToast.Type.ERROR,
                            "Login Failed",
                            message
                        )
                    }
                }

            } else {
                SuperToast.show(
                    this,
                    SuperToast.Type.ERROR,
                    "Google Failed",
                    "Google sign-in failed!"
                )
            }

        } catch (e: ApiException) {
            SuperToast.show(
                this,
                SuperToast.Type.ERROR,
                "Google Error",
                e.message ?: "Something went wrong"
            )
        }
    }


    // ---------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        runEntryAnimations()
    }


    // ---------------------------------------------------------
    private fun setupUI() {

        binding.apply {

            // LOGIN BUTTON
            btnLogin.setOnClickListener {

                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()

                when {
                    email.isEmpty() -> {
                        etEmail.error = "Enter email"
                        etEmail.requestFocus()
                    }

                    password.isEmpty() -> {
                        etPassword.error = "Enter password"
                        etPassword.requestFocus()
                    }

                    else -> {
                        showLoading(true)

                        viewModel.loginUser(email, password, "doctor") { success, message ->
                            showLoading(false)

                            if (success) {
                                SuperToast.show(
                                    this@DoctorLoginActivity,
                                    SuperToast.Type.SUCCESS,
                                    "Welcome!",
                                    message
                                )

                                val intent = Intent(
                                    this@DoctorLoginActivity,
                                    DoctorDashboardActivity::class.java
                                )
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                                startActivity(intent)
                                finish()

                            } else {
                                SuperToast.show(
                                    this@DoctorLoginActivity,
                                    SuperToast.Type.ERROR,
                                    "Login Failed",
                                    message
                                )
                            }
                        }
                    }
                }
            }

            // GOOGLE SIGN-IN
            btnGoogleDoctorSignin.setOnClickListener { startGoogleSignIn() }

            // SIGNUP
            tvSignup.setOnClickListener {
                startActivity(Intent(this@DoctorLoginActivity, DoctorSignupActivity::class.java))
            }

            // BACK TO ROLE PAGE
            tvBackToRole.setOnClickListener {
                startActivity(Intent(this@DoctorLoginActivity, RoleSelectActivity::class.java))
                finish()
            }
        }
    }


    // ---------------------------------------------------------
    private fun startGoogleSignIn() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val client = GoogleSignIn.getClient(this, gso)
        googleSignInLauncher.launch(client.signInIntent)
    }


    // ---------------------------------------------------------
    private fun observeViewModel() {
        viewModel.loading.observe(this) { showLoading(it) }

        viewModel.error.observe(this) {
            if (!it.isNullOrEmpty()) {
                SuperToast.show(
                    this,
                    SuperToast.Type.ERROR,
                    "Error",
                    it
                )
            }
        }
    }


    // ---------------------------------------------------------
    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

            etEmail.isEnabled = !isLoading
            etPassword.isEnabled = !isLoading
            btnLogin.isEnabled = !isLoading
            btnGoogleDoctorSignin.isEnabled = !isLoading

            progressBar.animate()
                .alpha(if (isLoading) 1f else 0f)
                .setDuration(250)
                .start()
        }
    }


    // ---------------------------------------------------------
    private fun runEntryAnimations() {

        binding.headerLayout.apply {
            translationY = -80f
            alpha = 0f
            animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(600)
                .setInterpolator(OvershootInterpolator())
                .start()
        }

        binding.scrollLogin.apply {
            translationY = 60f
            alpha = 0f

            animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(700)
                .setStartDelay(150)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .start()
        }

        binding.btnLogin.apply {
            scaleX = 0.7f
            scaleY = 0.7f
            alpha = 0f

            animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setStartDelay(400)
                .setDuration(450)
                .setInterpolator(OvershootInterpolator())
                .start()
        }

        binding.btnGoogleDoctorSignin.apply {
            alpha = 0f
            translationY = 40f

            animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay(450)
                .setDuration(500)
                .start()
        }

        binding.tvBackToRole.apply {
            translationY = 30f
            alpha = 0f

            animate()
                .translationY(0f)
                .alpha(1f)
                .setStartDelay(500)
                .setDuration(400)
                .start()
        }
    }
}
