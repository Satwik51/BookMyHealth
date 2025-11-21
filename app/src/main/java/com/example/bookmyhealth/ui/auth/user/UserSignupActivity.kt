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
import com.example.bookmyhealth.databinding.ActivitySignupBinding
import com.example.bookmyhealth.utils.SuperToast
import com.example.bookmyhealth.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class UserSignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val viewModel: AuthViewModel by viewModels()

    companion object {
        const val EXTRA_FROM_SIGNUP = "from_signup"
    }

    // GOOGLE SIGN-UP HANDLER
    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken

                if (idToken != null) {
                    showLoading(true)

                    viewModel.loginWithGoogle(idToken, "user") { success, message ->
                        showLoading(false)

                        if (success) {
                            SuperToast.show(
                                this,
                                SuperToast.Type.SUCCESS,
                                "Signup Successful",
                                message
                            )
                            openDashboardAndFinish()
                        } else {
                            SuperToast.show(
                                this,
                                SuperToast.Type.ERROR,
                                "Signup Failed",
                                message
                            )
                        }
                    }
                } else {
                    SuperToast.show(
                        this,
                        SuperToast.Type.ERROR,
                        "Google Sign-Up Failed",
                        "Unable to authenticate"
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        runEntryAnimations()
    }

    private fun setupUI() {
        binding.tvTitle.text = "Create Your Account"
        setupListeners()
        observeViewModel()
    }

    // SCREEN ANIMATIONS
    private fun runEntryAnimations() {
        animateTop(binding.headerLayout, 0)
        animateFade(binding.lottieAnimationView, 80)
        animateFade(binding.headerText, 120)
        animateTop(binding.tvTitle, 200)

        animateLeft(binding.tilName, 300)
        animateLeft(binding.tilEmail, 380)
        animateLeft(binding.tilPassword, 460)

        animateBounce(binding.btnSignup, 550)
        animateBottom(binding.btnGoogleSignup, 650)
        animateBottom(binding.tvLogin, 720)
    }

    private fun animateTop(v: View, delay: Long) {
        v.alpha = 0f
        v.translationY = -80f
        v.animate().alpha(1f).translationY(0f).setDuration(700)
            .setStartDelay(delay).setInterpolator(AccelerateDecelerateInterpolator()).start()
    }

    private fun animateLeft(v: View, delay: Long) {
        v.alpha = 0f
        v.translationX = -120f
        v.animate().alpha(1f).translationX(0f).setDuration(650)
            .setStartDelay(delay).setInterpolator(AccelerateDecelerateInterpolator()).start()
    }

    private fun animateBottom(v: View, delay: Long) {
        v.alpha = 0f
        v.translationY = 60f
        v.animate().alpha(1f).translationY(0f).setDuration(650)
            .setStartDelay(delay).setInterpolator(AccelerateDecelerateInterpolator()).start()
    }

    private fun animateBounce(v: View, delay: Long) {
        v.alpha = 0f
        v.scaleX = 0.6f; v.scaleY = 0.6f
        v.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(750)
            .setStartDelay(delay).setInterpolator(OvershootInterpolator(1.3f)).start()
    }

    private fun animateFade(v: View, delay: Long) {
        v.alpha = 0f
        v.animate().alpha(1f).setDuration(600)
            .setStartDelay(delay).setInterpolator(AccelerateDecelerateInterpolator()).start()
    }

    // GO TO DASHBOARD
    private fun openDashboardAndFinish() {
        val i = Intent(this, UserDashboardActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        i.putExtra(EXTRA_FROM_SIGNUP, true)
        startActivity(i)
        finishAffinity()
    }

    // BUTTON LISTENERS
    private fun setupListeners() {
        binding.apply {

            btnSignup.setOnClickListener {
                btnSignup.animate().scaleX(0.92f).scaleY(0.92f)
                    .setDuration(120).withEndAction {
                        btnSignup.animate().scaleX(1f).scaleY(1f).duration = 120
                    }.start()

                val name = etName.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()

                when {
                    name.isEmpty() -> { etName.error = "Enter name"; etName.requestFocus() }
                    email.isEmpty() -> { etEmail.error = "Enter email"; etEmail.requestFocus() }
                    password.isEmpty() -> { etPassword.error = "Enter password"; etPassword.requestFocus() }
                    password.length < 6 -> { etPassword.error = "Min 6 characters"; etPassword.requestFocus() }

                    else -> {
                        showLoading(true)

                        viewModel.registerUser(name, email, password, "user") { success, message ->
                            showLoading(false)

                            if (success) {
                                SuperToast.show(
                                    this@UserSignupActivity,
                                    SuperToast.Type.SUCCESS,
                                    "Signup Successful!",
                                    message
                                )
                                openDashboardAndFinish()
                            } else {
                                SuperToast.show(
                                    this@UserSignupActivity,
                                    SuperToast.Type.ERROR,
                                    "Signup Failed",
                                    message
                                )
                            }
                        }
                    }
                }
            }

            btnGoogleSignup.setOnClickListener { startGoogleSignIn() }

            tvLogin.setOnClickListener {
                startActivity(Intent(this@UserSignupActivity, UserLoginActivity::class.java))
                finish()
            }
        }
    }

    private fun startGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInLauncher.launch(GoogleSignIn.getClient(this, gso).signInIntent)
    }

    private fun observeViewModel() {
        viewModel.loading.observe(this) { showLoading(it) }

        viewModel.error.observe(this) {
            if (!it.isNullOrEmpty()) {
                showLoading(false)
                SuperToast.show(this, SuperToast.Type.ERROR, "Error", it)
            }
        }
    }

    private fun showLoading(b: Boolean) {
        binding.progressBar.visibility = if (b) View.VISIBLE else View.GONE

        binding.btnSignup.isEnabled = !b
        binding.btnGoogleSignup.isEnabled = !b

        binding.etName.isEnabled = !b
        binding.etEmail.isEnabled = !b
        binding.etPassword.isEnabled = !b
    }
}
