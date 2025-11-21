package com.example.bookmyhealth.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.example.bookmyhealth.R
import com.example.bookmyhealth.adapter.IntroAdapter
import com.example.bookmyhealth.data.model.IntroItem
import com.example.bookmyhealth.databinding.ActivityRoleSelectBinding
import com.example.bookmyhealth.ui.auth.doctor.DoctorLoginActivity
import com.example.bookmyhealth.ui.auth.user.UserLoginActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class RoleSelectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoleSelectBinding
    private lateinit var introAdapter: IntroAdapter

    private var autoSlideJob: Job? = null
    private var currentPage = 0
    private val slideInterval = 1600L

    private var selectedRole: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoleSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupIntroViewPager()
        setupRoleSelection()
        setupContinueButton()
        applyEntryAnimations()
        startAutoSlide()
    }

    // --------------------------------------------------------
    // VIEWPAGER
    // --------------------------------------------------------
    private fun setupIntroViewPager() {

        val introItems = listOf(
            IntroItem(R.drawable.doctor, "Welcome to BookMyHealth", "Book appointments easily."),
            IntroItem(R.drawable.sick, "Amazing Features", "Find top doctors & manage health."),
            IntroItem(R.drawable.health, "All in One App", "Your complete healthcare companion.")
        )

        introAdapter = IntroAdapter(introItems)
        binding.introViewPager.adapter = introAdapter

        // Fade + Scale transform
        binding.introViewPager.setPageTransformer { page, position ->
            val absPos = kotlin.math.abs(position)
            page.alpha = 1 - absPos
            page.scaleY = 0.85f + (1 - absPos) * 0.15f
        }

        // remove overscroll warning
        binding.introViewPager.getChildAt(0).overScrollMode = ViewPager2.OVER_SCROLL_NEVER
    }

    // --------------------------------------------------------
    // ROLE SELECTION WITH 3D TILT EFFECT
    // --------------------------------------------------------
    private fun setupRoleSelection() {

        val userCard = binding.btnUser
        val doctorCard = binding.btnDoctor

        val lottieUser: LottieAnimationView = binding.lottieUser
        val lottieDoctor: LottieAnimationView = binding.lottieDoctor

        // add clean 3D touch effect
        add3DTiltEffect(userCard)
        add3DTiltEffect(doctorCard)

        userCard.setOnClickListener {
            selectedRole = "user"
            highlightSelected("user")
            playLottie(lottieUser)
            animateCardBounce(userCard)
        }

        doctorCard.setOnClickListener {
            selectedRole = "doctor"
            highlightSelected("doctor")
            playLottie(lottieDoctor)
            animateCardBounce(doctorCard)
        }
    }

    // --------------------------------------------------------
    // 3D Tilt Effect (NO WARNINGS, PERFECT)
    // --------------------------------------------------------
    private fun add3DTiltEffect(view: View) {

        view.setOnTouchListener { v, event ->
            when (event.actionMasked) {

                MotionEvent.ACTION_DOWN -> {
                    v.animate()
                        .rotationX(8f)
                        .rotationY(-8f)
                        .scaleX(0.95f)
                        .scaleY(0.95f)
                        .setDuration(120)
                        .start()
                }

                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_CANCEL -> {
                    v.animate()
                        .rotationX(0f)
                        .rotationY(0f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setInterpolator(OvershootInterpolator(1.2f))
                        .setDuration(260)
                        .start()
                }
            }
            // Let click event work properly
            false
        }
    }

    // --------------------------------------------------------
    // CARD HIGHLIGHT
    // --------------------------------------------------------
    private fun highlightSelected(role: String) {

        val selectedOutline = R.drawable.bg_role_selected
        val unselectedOutline = R.drawable.bg_role_unselected

        if (role == "user") {
            binding.btnUser.setBackgroundResource(selectedOutline)
            binding.btnDoctor.setBackgroundResource(unselectedOutline)
        } else {
            binding.btnDoctor.setBackgroundResource(selectedOutline)
            binding.btnUser.setBackgroundResource(unselectedOutline)
        }

        animateContinueButton()
    }

    // --------------------------------------------------------
    // LOTTIE PLAY (WITH SPEED BOOST)
    // --------------------------------------------------------
    private fun playLottie(view: LottieAnimationView) {
        view.speed = 2.5f   // fast & smooth
        view.progress = 0f
        view.playAnimation()
    }

    // --------------------------------------------------------
    // BOUNCE ANIMATION
    // --------------------------------------------------------
    private fun animateCardBounce(v: View) {
        v.animate()
            .scaleX(0.92f)
            .scaleY(0.92f)
            .setDuration(120)
            .withEndAction {
                v.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setInterpolator(OvershootInterpolator(1.3f))
                    .setDuration(260)
                    .start()
            }.start()
    }

    // --------------------------------------------------------
    // CONTINUE BUTTON + PULSE
    // --------------------------------------------------------
    private fun setupContinueButton() {
        binding.btnContinue.alpha = 0.35f
        binding.btnContinue.isEnabled = false

        binding.btnContinue.setOnClickListener {
            when (selectedRole) {
                "user" -> startActivity(Intent(this, UserLoginActivity::class.java))
                "doctor" -> startActivity(Intent(this, DoctorLoginActivity::class.java))
            }
        }
    }

    private fun animateContinueButton() {

        binding.btnContinue.isEnabled = true

        binding.btnContinue.animate()
            .alpha(1f)
            .setDuration(300)
            .start()

        // pulse animation
        binding.btnContinue.scaleX = 0.9f
        binding.btnContinue.scaleY = 0.9f

        binding.btnContinue.animate()
            .scaleX(1f)
            .scaleY(1f)
            .setInterpolator(OvershootInterpolator(1.6f))
            .setDuration(360)
            .start()
    }

    // --------------------------------------------------------
    // ENTRY ANIMATIONS
    // --------------------------------------------------------
    private fun applyEntryAnimations() {

        val fade = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        binding.btnUser.startAnimation(fade)
        binding.btnDoctor.startAnimation(fade)

        binding.btnContinue.apply {
            alpha = 0f
            translationY = 60f
            animate()
                .alpha(0.35f)
                .translationY(0f)
                .setDuration(600)
                .setStartDelay(200)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .start()
        }
    }

    // --------------------------------------------------------
    // AUTO-SLIDE VIEWPAGER
    // --------------------------------------------------------
    private fun startAutoSlide() {
        autoSlideJob?.cancel()

        autoSlideJob = lifecycleScope.launch {
            while (isActive) {
                delay(slideInterval)
                if (::introAdapter.isInitialized && introAdapter.itemCount > 1) {
                    currentPage = (currentPage + 1) % introAdapter.itemCount
                    binding.introViewPager.setCurrentItem(currentPage, true)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        autoSlideJob?.cancel()
    }
}
