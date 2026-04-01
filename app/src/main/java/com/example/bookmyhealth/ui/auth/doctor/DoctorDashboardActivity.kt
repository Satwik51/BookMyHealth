package com.example.bookmyhealth.ui.auth.doctor

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.bookmyhealth.R
import com.example.bookmyhealth.adapter.AppointmentAdapter
import com.example.bookmyhealth.adapter.LottieSliderAdapter
import com.example.bookmyhealth.data.model.Appointment
import com.example.bookmyhealth.data.model.SlideItem
import com.example.bookmyhealth.databinding.ActivityDoctorDashboardBinding
import com.example.bookmyhealth.ui.auth.RoleSelectActivity
import com.example.bookmyhealth.ui.profile.DoctorProfileActivity
import com.example.bookmyhealth.utils.SuperToast
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.zxing.integration.android.IntentIntegrator

class DoctorDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorDashboardBinding
    private lateinit var appointmentAdapter: AppointmentAdapter

    private val appointmentList = mutableListOf<Appointment>()
    private val filteredList = mutableListOf<Appointment>()

    private lateinit var sliderAdapter: LottieSliderAdapter
    private val sliderHandler = Handler(Looper.getMainLooper())
    private var currentPage = 0

    private val auth by lazy { FirebaseAuth.getInstance() }

    private val DB_URL = "https://bookmyhealth-5920a-default-rtdb.asia-southeast1.firebasedatabase.app"

    private val dbRefAppointments: DatabaseReference by lazy {
        FirebaseDatabase.getInstance(DB_URL).getReference("appointments")
    }

    private val dbRefDoctors: DatabaseReference by lazy {
        FirebaseDatabase.getInstance(DB_URL).getReference("doctors")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupDrawer()
        loadDoctorName()
        loadGooglePhotoTop()
        setupLottieSlider()
        setupSearchFilter()
        fetchAppointments()

        animateHeader()
        animateSlider()
        animateSearchBar()
        animateQuickActions()

        val fromSignup = intent.getBooleanExtra("EXTRA_FROM_DOCTOR_SIGNUP", false)
        if (fromSignup && savedInstanceState == null) {
            binding.root.post { showDoctorSignupSuccessDialog() }
        }
    }

    private fun showDoctorSignupSuccessDialog() {
        val view = layoutInflater.inflate(R.layout.alert_success, null)
        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(false)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        view.findViewById<MaterialButton>(R.id.btnDialogOk).setOnClickListener {
            dialog.dismiss()
        }

        view.findViewById<MaterialButton>(R.id.btnGoProfile).setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this, DoctorProfileActivity::class.java))
        }
    }
    private fun setupUI() {
        setupRecyclerView()

        binding.btnApprovedList.setOnClickListener {
            startActivity(Intent(this, DoctorApprovedListActivity::class.java))
        }

        binding.btnProfile.setOnClickListener {
            startActivity(Intent(this, DoctorProfileActivity::class.java))
        }

        binding.btnScanQR.setOnClickListener {

            val integrator = IntentIntegrator(this)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            integrator.setPrompt("\n\n\n📱 BookMyHealth Verifier\nScan Patient Booking QR")
            integrator.setBeepEnabled(true)

            // 🔥🔥 FINAL FIX (FORCE PORTRAIT)
            integrator.setCaptureActivity(
                com.example.bookmyhealth.utils.CaptureActivityPortrait::class.java
            )

            integrator.setOrientationLocked(true)

            integrator.initiateScan()
        }

        binding.btnRefreshDoctor.setOnClickListener {
            binding.btnRefreshDoctor.animate().rotationBy(360f).setDuration(600)
                .setInterpolator(OvershootInterpolator()).start()
            fetchAppointments()
            SuperToast.show(this, SuperToast.Type.SUCCESS, "Refreshing", "Data updated")
        }

        binding.btnFilterDoctor.setOnClickListener { openFilterDialog() }
    }

    // 🔥 FIXED QR SCAN
    // 🔥 ONLY CHANGE: onActivityResult + debug logs + safe cleaning

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null) {

            if (result.contents != null) {

                val rawData = result.contents

                Log.d("QR_DEBUG", "==============================")
                Log.d("QR_DEBUG", "RAW DATA = [$rawData]")
                Log.d("QR_DEBUG", "RAW LENGTH = ${rawData.length}")

                // ✅ SAFE CLEAN (NO OVER CLEANING)
                val cleanId = rawData
                    .trim()
                    .replace("\n", "")
                    .replace("\r", "")
                    .replace(" ", "")

                Log.d("QR_DEBUG", "CLEAN ID = [$cleanId]")
                Log.d("QR_DEBUG", "CLEAN LENGTH = ${cleanId.length}")

                // 🔥 Character by character debug
                cleanId.forEachIndexed { index, c ->
                    Log.d("QR_DEBUG", "CHAR[$index] = '$c' ASCII=${c.code}")
                }

                if (cleanId.isNotEmpty()) {
                    fetchAndVerifyPatient(cleanId)
                } else {
                    SuperToast.show(this, SuperToast.Type.ERROR, "Scan Error", "Invalid QR")
                }

            } else {
                Log.d("QR_DEBUG", "Scan Cancelled")
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun fetchAndVerifyPatient(appId: String) {

        Log.d("QR_DEBUG", "🔥 FETCHING ID = [$appId]")

        binding.progressBar.visibility = View.VISIBLE

        dbRefAppointments.child(appId).get()
            .addOnSuccessListener { appSnap ->

                Log.d("QR_DEBUG", "EXISTS = ${appSnap.exists()}")
                Log.d("QR_DEBUG", "KEY FROM DB = ${appSnap.key}")

                if (appSnap.exists()) {

                    val userId = appSnap.child("userId").value?.toString() ?: ""

                    Log.d("QR_DEBUG", "USER ID = $userId")

                    if (userId.isNotEmpty()) {

                        FirebaseDatabase.getInstance(DB_URL)
                            .getReference("users")
                            .child(userId)
                            .get()
                            .addOnSuccessListener { userSnap ->

                                binding.progressBar.visibility = View.GONE

                                val name = userSnap.child("name").value?.toString() ?: "Unknown"
                                val age = userSnap.child("age").value?.toString() ?: "N/A"
                                val img = userSnap.child("imageUrl").value?.toString() ?: ""
                                val email = userSnap.child("email").value?.toString() ?: "N/A"
                                val phone = userSnap.child("phone").value?.toString() ?: "N/A"
                                val address = userSnap.child("address").value?.toString() ?: "N/A"

                                val dr = appSnap.child("doctorName").value?.toString() ?: "Doctor"
                                val date = appSnap.child("date").value?.toString() ?: "N/A"
                                val time = appSnap.child("time").value?.toString() ?: "N/A"
                                val token = appSnap.child("tokenNumber").value?.toString() ?: "0"
                                val status = appSnap.child("status").value?.toString() ?: "Pending"

                                showPatientVerificationDialog(
                                    name, email, phone, token, status,
                                    img, address, age, dr, date, time
                                )
                            }
                            .addOnFailureListener {
                                binding.progressBar.visibility = View.GONE
                                Log.e("QR_DEBUG", "❌ USER FETCH ERROR = ${it.message}")
                            }

                    } else {
                        binding.progressBar.visibility = View.GONE
                        Log.d("QR_DEBUG", "❌ USER ID EMPTY")
                    }

                } else {
                    binding.progressBar.visibility = View.GONE
                    Log.d("QR_DEBUG", "❌ APPOINTMENT NOT FOUND")
                    SuperToast.show(this, SuperToast.Type.ERROR, "Not Found", "Appointment not found")
                }

            }
            .addOnFailureListener {
                binding.progressBar.visibility = View.GONE
                Log.e("QR_DEBUG", "❌ FIREBASE ERROR = ${it.message}")
            }
    }

    private fun showPatientVerificationDialog(name: String, email: String, phone: String, token: String, status: String, img: String, address: String, age: String, dr: String, date: String, time: String) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_patient_verify, null)
        val dialog = AlertDialog.Builder(this).setView(dialogView).create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogView.findViewById<TextView>(R.id.tvVerifyName).text = "$name ($age)"
        dialogView.findViewById<TextView>(R.id.tvVerifyDoctor).text = "Doctor: $dr"
        dialogView.findViewById<TextView>(R.id.tvVerifyDateTime).text = "Date: $date | Time: $time"
        dialogView.findViewById<TextView>(R.id.tvVerifyDetails).text = "Email: $email\nPhone: $phone\nToken: #$token"
        dialogView.findViewById<TextView>(R.id.tvVerifyAddress).text = address

        val tvStatus = dialogView.findViewById<TextView>(R.id.tvVerifyStatus)
        tvStatus.text = "Status: $status"

        if (status.equals("Approved", ignoreCase = true)) {
            tvStatus.setTextColor(android.graphics.Color.parseColor("#2E7D32"))
        } else {
            tvStatus.setTextColor(android.graphics.Color.parseColor("#D32F2F"))
        }

        val ivUser = dialogView.findViewById<ImageView>(R.id.ivVerifyUser)
        Glide.with(this)
            .load(img)
            .placeholder(R.drawable.profile_placeholder)
            .circleCrop()
            .into(ivUser)

        dialogView.findViewById<MaterialButton>(R.id.btnVerifyClose).setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun loadGooglePhotoTop() {
        auth.currentUser?.photoUrl?.let {
            Glide.with(this).load(it).placeholder(R.drawable.placeholder_profile).circleCrop().into(binding.ivDoctorProfile)
        }
    }

    private fun openFilterDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_filter_doctor, null)
        val dialog = AlertDialog.Builder(this).setView(dialogView).create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialogView.findViewById<MaterialCardView>(R.id.cardFilterToday).setOnClickListener { applyDateFilter("TODAY"); dialog.dismiss() }
        dialogView.findViewById<MaterialCardView>(R.id.cardFilterUpcoming).setOnClickListener { applyDateFilter("UPCOMING"); dialog.dismiss() }
        dialogView.findViewById<MaterialCardView>(R.id.cardFilterAll).setOnClickListener { applyDateFilter("ALL"); dialog.dismiss() }
        dialogView.findViewById<MaterialButton>(R.id.btnClearFilter).setOnClickListener {
            binding.etSearchDoctor.setText("")
            fetchAppointments()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun applyDateFilter(type: String) {
        val cal = java.util.Calendar.getInstance()
        val today = String.format("%04d-%02d-%02d", cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH) + 1, cal.get(java.util.Calendar.DAY_OF_MONTH))
        filteredList.clear()
        when (type) {
            "TODAY" -> filteredList.addAll(appointmentList.filter { it.date == today })
            "UPCOMING" -> filteredList.addAll(appointmentList.filter { it.date > today })
            "ALL" -> filteredList.addAll(appointmentList)
        }
        appointmentAdapter.submitList(filteredList.toList())
        toggleEmptyState(filteredList.isEmpty())
    }

    private fun setupSearchFilter() {
        binding.etSearchDoctor.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val q = s.toString().lowercase()
                filteredList.clear()
                filteredList.addAll(if (q.isEmpty()) appointmentList else appointmentList.filter { it.userName.lowercase().contains(q) || it.date.contains(q) })
                appointmentAdapter.submitList(filteredList.toList())
                toggleEmptyState(filteredList.isEmpty())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupDrawer() {
        binding.ivDoctorProfile.setOnClickListener { binding.doctorDrawerLayout.openDrawer(GravityCompat.START) }
        val headerView = binding.doctorNavigationView.getHeaderView(0)
        val uid = auth.currentUser?.uid ?: return
        dbRefDoctors.child(uid).get().addOnSuccessListener {
            headerView.findViewById<TextView>(R.id.headerDoctorName).text = "Dr. ${it.child("name").value}"
        }
        binding.doctorNavigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> binding.doctorDrawerLayout.closeDrawer(GravityCompat.START)
                R.id.nav_profile -> startActivity(Intent(this, DoctorProfileActivity::class.java))
                R.id.nav_logout -> {
                    auth.signOut()
                    startActivity(Intent(this, RoleSelectActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                }
            }
            true
        }
    }

    private fun loadDoctorName() {
        val uid = auth.currentUser?.uid ?: return
        dbRefDoctors.child(uid).get().addOnSuccessListener { snapshot ->
            binding.tvDoctorName.text = "Dr. ${snapshot.child("name").value ?: "Doctor"}"
        }
    }

    private fun setupLottieSlider() {
        val slides = listOf(SlideItem(R.raw.doctor_slide_1, "Manage Appointments"), SlideItem(R.raw.doctor_slide_2, "Dashboard"), SlideItem(R.raw.doctor_slide_3, "Profile"))
        sliderAdapter = LottieSliderAdapter(slides)
        binding.lottieSlider.adapter = sliderAdapter
        sliderHandler.postDelayed(sliderRunnable, 3000)
    }

    private val sliderRunnable = object : Runnable {
        override fun run() {
            if (::sliderAdapter.isInitialized) {
                if (currentPage >= sliderAdapter.itemCount) currentPage = 0
                binding.lottieSlider.setCurrentItem(currentPage++, true)
                sliderHandler.postDelayed(this, 3000)
            }
        }
    }

    private fun setupRecyclerView() {
        appointmentAdapter = AppointmentAdapter(
            onApprove = { a -> updateStatus(a, "Approved") },
            onReject = { a -> updateStatus(a, "Rejected") }
        )
        binding.rvAppointments.layoutManager = LinearLayoutManager(this)
        binding.rvAppointments.adapter = appointmentAdapter
    }

    private fun fetchAppointments() {
        val doctorId = auth.currentUser?.uid ?: return
        binding.progressBar.visibility = View.VISIBLE
        dbRefAppointments.orderByChild("doctorId").equalTo(doctorId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                appointmentList.clear()
                for (child in snapshot.children) {
                    val appt = child.getValue(Appointment::class.java)?.copy(appointmentId = child.key ?: "")
                    if (appt != null && appt.status == "Pending") appointmentList.add(appt)
                }
                filteredList.clear(); filteredList.addAll(appointmentList)
                binding.progressBar.visibility = View.GONE
                toggleEmptyState(filteredList.isEmpty())
                appointmentAdapter.submitList(filteredList.toList())
            }
            override fun onCancelled(error: DatabaseError) { binding.progressBar.visibility = View.GONE }
        })
    }

    private fun toggleEmptyState(isEmpty: Boolean) {
        binding.emptyContainer.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.rvAppointments.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    private fun updateStatus(appt: Appointment?, status: String) {
        if (appt?.appointmentId.isNullOrBlank()) return
        dbRefAppointments.child(appt!!.appointmentId).child("status").setValue(status)
            .addOnSuccessListener { SuperToast.show(this, SuperToast.Type.SUCCESS, "Updated", "Marked as $status") }
    }

    override fun onDestroy() {
        super.onDestroy()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    private fun animateHeader() { binding.headerDoctor.animate().translationY(0f).alpha(1f).setDuration(650).start() }
    private fun animateSlider() { binding.lottieSlider.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(650).start() }
    private fun animateQuickActions() { binding.layoutActions.animate().translationY(0f).alpha(1f).setDuration(650).start() }
    private fun animateSearchBar() { binding.layoutSearchBarDoctor.animate().translationY(0f).alpha(1f).setDuration(550).start() }
}