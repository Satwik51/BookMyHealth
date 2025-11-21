package com.example.bookmyhealth.ui.auth.user

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookmyhealth.R
import com.example.bookmyhealth.adapter.UserAppointmentAdapter
import com.example.bookmyhealth.databinding.ActivityUserAppointmentsBinding
import com.example.bookmyhealth.utils.SuperToast
import com.example.bookmyhealth.viewmodel.AppointmentViewModel
import com.google.firebase.auth.FirebaseAuth

class UserAppointmentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserAppointmentsBinding
    private val viewModel: AppointmentViewModel by viewModels()
    private lateinit var adapter: UserAppointmentAdapter
    private val auth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserAppointmentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupObservers()
        setupRefreshButton()

        animateHeader()
        loadAppointments()
    }

    private fun loadAppointments() {
        val userId = auth.currentUser?.uid

        if (userId != null) {
            Log.d("UserAppointments", "Loading appointments for $userId")
            viewModel.getAppointmentsForUser(userId)
        } else {
            SuperToast.show(
                this,
                SuperToast.Type.ERROR,
                "Error",
                "User not logged in"
            )
        }
    }

    // -------------------------------------------------
    private fun setupRefreshButton() {
        binding.btnRefresh.setOnClickListener {

            binding.btnRefresh.animate()
                .rotationBy(360f)
                .setInterpolator(OvershootInterpolator())
                .setDuration(600)
                .start()

            loadAppointments()
        }
    }

    // -------------------------------------------------
    private fun setupRecyclerView() {
        adapter = UserAppointmentAdapter()
        binding.recyclerAppointments.layoutManager = LinearLayoutManager(this)
        binding.recyclerAppointments.adapter = adapter

        binding.recyclerAppointments.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(this, R.anim.recycler_fade_in)
    }

    // -------------------------------------------------
    private fun setupObservers() {

        viewModel.loading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.error.observe(this) { error ->
            if (!error.isNullOrEmpty()) {
                SuperToast.show(
                    this,
                    SuperToast.Type.WARNING,
                    "Error",
                    error
                )
                showLoading(false)
            }
        }

        viewModel.appointments.observe(this) { appointments ->
            showLoading(false)

            if (appointments.isNullOrEmpty()) {
                showEmptyState(true)
            } else {
                showEmptyState(false)
                adapter.submitList(appointments)

                binding.recyclerAppointments.scheduleLayoutAnimation()
            }
        }
    }

    // -------------------------------------------------
    private fun showLoading(isLoading: Boolean) {

        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.progressBar.alpha = 0f
            binding.progressBar.animate().alpha(1f).setDuration(250).start()

            binding.recyclerAppointments.visibility = View.INVISIBLE
            binding.emptyLayout.visibility = View.INVISIBLE

        } else {
            binding.progressBar.animate()
                .alpha(0f)
                .setDuration(200)
                .withEndAction { binding.progressBar.visibility = View.GONE }
                .start()
        }
    }

    // -------------------------------------------------
    private fun showEmptyState(isEmpty: Boolean) {

        if (isEmpty) {

            binding.emptyLayout.visibility = View.VISIBLE
            binding.recyclerAppointments.visibility = View.INVISIBLE

            binding.lottieEmpty.visibility = View.VISIBLE
            binding.lottieEmpty.playAnimation()

            binding.emptyLayout.scaleX = 0.8f
            binding.emptyLayout.scaleY = 0.8f
            binding.emptyLayout.alpha = 0f

            binding.emptyLayout.animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(450)
                .setInterpolator(OvershootInterpolator())
                .start()

        } else {
            binding.emptyLayout.visibility = View.GONE
            binding.recyclerAppointments.visibility = View.VISIBLE
            binding.lottieEmpty.cancelAnimation()
            binding.lottieEmpty.visibility = View.GONE
        }
    }

    // -------------------------------------------------
    private fun animateHeader() {
        binding.tvHeader.translationY = -80f
        binding.tvHeader.alpha = 0f

        binding.tvHeader.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(600)
            .setStartDelay(150)
            .start()
    }
}
