package com.example.bookmyhealth.ui.auth.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bookmyhealth.R
import com.example.bookmyhealth.databinding.ActivityUserLoginBinding
import com.example.bookmyhealth.utils.SuperToast
import com.example.bookmyhealth.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserLoginBinding
    private val viewModel: AuthViewModel by viewModels()

    // ============================================================================================
    // ============================================================================================
    // ⭐ GOOGLE LOGIN HANDLER
    // ============================================================================================
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            val account = task.getResult(ApiException::class.java)
            val idToken = account?.idToken

            if (idToken != null) {

                showLoading(true)

                viewModel.loginWithGoogle(idToken, "user") { success, message ->
                    showLoading(false)

                    if (success) {
                        verifyUserExistsInDB(message)
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
                    "Google Sign-In Failed",
                    "Unable to authenticate user"
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

    // ============================================================================================
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        runEntryAnimations()
    }

    private fun setupUI() {
        setupListeners()
        observeViewModel()
    }

    // ============================================================================================
    private fun setupListeners() {

        binding.apply {

            // ⭐ EMAIL LOGIN
            btnLogin.setOnClickListener {

                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()

                if (email.isEmpty()) {
                    etEmail.error = "Email required"
                    etEmail.requestFocus()
                    return@setOnClickListener
                }

                if (password.isEmpty()) {
                    etPassword.error = "Password required"
                    etPassword.requestFocus()
                    return@setOnClickListener
                }

                showLoading(true)

                viewModel.loginUser(email, password, "user") { success, message ->
                    showLoading(false)

                    if (success) {
                        verifyUserExistsInDB(message)
                    } else {
                        SuperToast.show(
                            this@UserLoginActivity,
                            SuperToast.Type.ERROR,
                            "Login Failed",
                            message
                        )
                    }
                }
            }

            // ⭐ GOOGLE LOGIN
            btnGoogleUserSignin.setOnClickListener {
                startGoogleSignIn()
            }

            // ⭐ SIGNUP
            tvSignup.setOnClickListener {
                startActivity(Intent(this@UserLoginActivity, UserSignupActivity::class.java))
            }
        }
    }

    // ============================================================================================
    private fun startGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        GoogleSignIn.getClient(this, gso)
            .also { googleSignInLauncher.launch(it.signInIntent) }
    }

    // ============================================================================================
    // ⭐ BLOCK LOGIN IF USER NOT REGISTERED IN REALTIME DB
    // ============================================================================================
    private fun verifyUserExistsInDB(message: String) {

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val userRef = FirebaseDatabase.getInstance(
            "https://bookmyhealth-5920a-default-rtdb.asia-southeast1.firebasedatabase.app"
        ).getReference("users").child(uid)

        userRef.get().addOnSuccessListener { snapshot ->

            if (!snapshot.exists()) {

                // ❌ USER NOT FOUND — STOP LOGIN
                SuperToast.show(
                    this,
                    SuperToast.Type.ERROR,
                    "Signup Required",
                    "Please sign up before logging in."
                )

                FirebaseAuth.getInstance().signOut()
                return@addOnSuccessListener
            }

            // ⭐ USER FOUND → PROCEED
            SuperToast.show(
                this,
                SuperToast.Type.SUCCESS,
                "Welcome!",
                message
            )

            startActivity(Intent(this, UserDashboardActivity::class.java))
            finishAffinity()
        }
    }

    // ============================================================================================
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
                showLoading(false)
            }
        }
    }

    // ============================================================================================
    private fun showLoading(isLoading: Boolean) {

        binding.apply {
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

            etEmail.isEnabled = !isLoading
            etPassword.isEnabled = !isLoading
            btnLogin.isEnabled = !isLoading
            btnGoogleUserSignin.isEnabled = !isLoading

            progressBar.animate()
                .alpha(if (isLoading) 1f else 0f)
                .setDuration(250)
                .start()
        }
    }

    // ============================================================================================
    private fun runEntryAnimations() {

        binding.headerLayout.apply {
            translationY = -90f
            alpha = 0f
            animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(600)
                .setInterpolator(OvershootInterpolator())
                .start()
        }

        binding.scrollLogin.apply {
            alpha = 0f
            translationY = 50f
            animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(700)
                .setStartDelay(120)
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
                .setDuration(450)
                .setStartDelay(350)
                .setInterpolator(OvershootInterpolator())
                .start()
        }

        binding.btnGoogleUserSignin.apply {
            alpha = 0f
            translationY = 40f
            animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(500)
                .setStartDelay(450)
                .start()
        }

        binding.tvSignup.apply {
            alpha = 0f
            translationY = 25f
            animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay(550)
                .setDuration(450)
                .start()
        }
    }
}
