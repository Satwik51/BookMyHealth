package com.example.bookmyhealth.ui.auth.doctor

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookmyhealth.R
import com.example.bookmyhealth.adapter.AppointmentAdapter
import com.example.bookmyhealth.data.model.Appointment
import com.example.bookmyhealth.databinding.ActivityDoctorApprovedListBinding
import com.example.bookmyhealth.utils.SuperToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DoctorApprovedListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorApprovedListBinding
    private lateinit var adapter: AppointmentAdapter

    private val auth by lazy { FirebaseAuth.getInstance() }

    private val dbRef: DatabaseReference by lazy {
        FirebaseDatabase.getInstance(
            "https://bookmyhealth-5920a-default-rtdb.asia-southeast1.firebasedatabase.app"
        ).getReference("appointments")
    }

    private var listener: ValueEventListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorApprovedListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        fetchApprovedAppointments()

        runEntryAnimation()
    }

    // ---------------------------------------------------------
    private fun setupToolbar() {
        val toolbar = binding.toolbarDoctorInclude.toolbarDoctor
        toolbar.title = "Approved Appointments"
        toolbar.setNavigationOnClickListener { finish() }

        toolbar.translationY = -120f
        toolbar.alpha = 0f
        toolbar.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(500)
            .setInterpolator(OvershootInterpolator())
            .start()
    }

    // ---------------------------------------------------------
    private fun setupRecyclerView() {
        adapter = AppointmentAdapter(
            onApprove = {},
            onReject = {}
        )

        binding.rvApproved.layoutManager = LinearLayoutManager(this)
        binding.rvApproved.adapter = adapter

        binding.rvApproved.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(this, R.anim.recycler_fade_in)
    }

    // ---------------------------------------------------------
    private fun fetchApprovedAppointments() {
        val doctorId = auth.currentUser?.uid
        if (doctorId == null) {
            SuperToast.show(
                this,
                SuperToast.Type.ERROR,
                "Error",
                "Doctor not logged in!"
            )
            return
        }

        showLoading(true)

        listener = dbRef.orderByChild("doctorId").equalTo(doctorId)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    val approvedList = mutableListOf<Appointment>()

                    for (child in snapshot.children) {
                        val appt = child.getValue(Appointment::class.java)
                        if (appt != null && appt.status == "Approved") {
                            approvedList.add(appt)
                        }
                    }

                    showLoading(false)

                    if (approvedList.isEmpty()) {
                        showEmpty(true)
                    } else {
                        showEmpty(false)
                        adapter.submitList(approvedList)
                        binding.rvApproved.scheduleLayoutAnimation()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showLoading(false)

                    SuperToast.show(
                        this@DoctorApprovedListActivity,
                        SuperToast.Type.ERROR,
                        "Failed",
                        error.message
                    )
                }
            })
    }

    // ---------------------------------------------------------
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {

            binding.progressBar.visibility = View.VISIBLE
            binding.progressBar.alpha = 0f
            binding.progressBar.animate()
                .alpha(1f)
                .setDuration(250)
                .start()

            binding.emptyLayout.visibility = View.INVISIBLE
            binding.rvApproved.visibility = View.INVISIBLE

        } else {

            binding.progressBar.animate()
                .alpha(0f)
                .setDuration(200)
                .withEndAction { binding.progressBar.visibility = View.GONE }
                .start()
        }
    }

    // ---------------------------------------------------------
    private fun showEmpty(show: Boolean) {
        if (show) {
            binding.emptyLayout.visibility = View.VISIBLE
            binding.rvApproved.visibility = View.INVISIBLE

            binding.lottieEmpty.playAnimation()

            binding.emptyLayout.scaleX = 0.8f
            binding.emptyLayout.scaleY = 0.8f
            binding.emptyLayout.alpha = 0f

            binding.emptyLayout.animate()
                .scaleX(1f).scaleY(1f)
                .alpha(1f)
                .setDuration(500)
                .setInterpolator(OvershootInterpolator())
                .start()

        } else {
            binding.emptyLayout.visibility = View.GONE
            binding.lottieEmpty.cancelAnimation()
            binding.rvApproved.visibility = View.VISIBLE
        }
    }

    // ---------------------------------------------------------
    private fun runEntryAnimation() {
        binding.rvApproved.translationY = 40f
        binding.rvApproved.alpha = 0f

        binding.rvApproved.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(500)
            .setStartDelay(150)
            .start()
    }

    override fun onDestroy() {
        super.onDestroy()
        listener?.let { dbRef.removeEventListener(it) }
        binding.lottieEmpty.cancelAnimation()
    }
}
