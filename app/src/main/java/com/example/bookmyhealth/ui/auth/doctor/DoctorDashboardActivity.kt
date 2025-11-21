package com.example.bookmyhealth.ui.auth.doctor

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
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

class DoctorDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorDashboardBinding
    private lateinit var appointmentAdapter: AppointmentAdapter

    private val appointmentList = mutableListOf<Appointment>()
    private val filteredList = mutableListOf<Appointment>()

    private lateinit var sliderAdapter: LottieSliderAdapter
    private val sliderHandler = Handler(Looper.getMainLooper())
    private var currentPage = 0

    private val auth by lazy { FirebaseAuth.getInstance() }

    private val dbRefAppointments: DatabaseReference by lazy {
        FirebaseDatabase.getInstance(
            "https://bookmyhealth-5920a-default-rtdb.asia-southeast1.firebasedatabase.app"
        ).getReference("appointments")
    }

    private val dbRefDoctors: DatabaseReference by lazy {
        FirebaseDatabase.getInstance(
            "https://bookmyhealth-5920a-default-rtdb.asia-southeast1.firebasedatabase.app"
        ).getReference("doctors")
    }

    private var appointmentListener: ValueEventListener? = null

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

        val fromSignup = intent.getBooleanExtra(DoctorSignupActivity.EXTRA_FROM_DOCTOR_SIGNUP, false)
        if (fromSignup && savedInstanceState == null) {
            binding.root.post { showDoctorSignupSuccessDialog() }
        }
    }

    private fun showDoctorSignupSuccessDialog() {
        val view = layoutInflater.inflate(R.layout.alert_success, null)

        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(false)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        val successIcon = view.findViewById<ImageView>(R.id.successIcon)
        val okBtn = view.findViewById<MaterialButton>(R.id.btnDialogOk)
        val goProfileBtn = view.findViewById<MaterialButton>(R.id.btnGoProfile)

        view.scaleX = 0.85f
        view.scaleY = 0.85f
        view.alpha = 0f

        view.animate()
            .scaleX(1f).scaleY(1f).alpha(1f)
            .setDuration(350)
            .setStartDelay(50)
            .setInterpolator(OvershootInterpolator(1.2f))
            .start()

        successIcon.scaleX = 0f
        successIcon.scaleY = 0f

        successIcon.animate()
            .scaleX(1f).scaleY(1f)
            .setDuration(520)
            .setStartDelay(150)
            .setInterpolator(OvershootInterpolator(1.3f))
            .start()

        okBtn.setOnClickListener { dialog.dismiss() }

        goProfileBtn.setOnClickListener {
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

        binding.btnRefreshDoctor.setOnClickListener {

            binding.btnRefreshDoctor.animate()
                .rotationBy(360f)
                .setDuration(600)
                .setInterpolator(OvershootInterpolator())
                .start()

            binding.etSearchDoctor.setText("")
            filteredList.clear()
            filteredList.addAll(appointmentList)
            appointmentAdapter.submitList(filteredList.toList())

            SuperToast.show(
                this,
                SuperToast.Type.SUCCESS,
                "Refreshing",
                "Updating appointments…"
            )

            binding.rvAppointments.smoothScrollToPosition(0)
            fetchAppointments()
        }

        binding.btnFilterDoctor.setOnClickListener {
            openFilterDialog()
        }
    }

    private fun loadGooglePhotoTop() {
        val user = auth.currentUser
        val photoUrl = user?.photoUrl

        if (photoUrl != null) {
            Glide.with(this)
                .load(photoUrl)
                .placeholder(R.drawable.placeholder_profile)
                .circleCrop()
                .into(binding.ivDoctorProfile)
        }
    }

    private fun openFilterDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_filter_doctor, null)

        val dialog = android.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val cardToday = dialogView.findViewById<MaterialCardView>(R.id.cardFilterToday)
        val cardUpcoming = dialogView.findViewById<MaterialCardView>(R.id.cardFilterUpcoming)
        val cardAll = dialogView.findViewById<MaterialCardView>(R.id.cardFilterAll)
        val btnClearFilter = dialogView.findViewById<MaterialButton>(R.id.btnClearFilter)

        cardToday.setOnClickListener {
            applyDateFilter("TODAY")
            dialog.dismiss()
        }

        cardUpcoming.setOnClickListener {
            applyDateFilter("UPCOMING")
            dialog.dismiss()
        }

        cardAll.setOnClickListener {
            applyDateFilter("ALL")
            dialog.dismiss()
        }

        btnClearFilter.setOnClickListener {
            binding.etSearchDoctor.setText("")
            filteredList.clear()
            filteredList.addAll(appointmentList)
            appointmentAdapter.submitList(filteredList.toList())

            SuperToast.show(
                this,
                SuperToast.Type.WARNING,
                "Filter Cleared",
                "All filter options reset"
            )

            dialog.dismiss()
        }

        dialog.show()
    }

    private fun getTodayString(): String {
        val cal = java.util.Calendar.getInstance()
        return String.format(
            "%04d-%02d-%02d",
            cal.get(java.util.Calendar.YEAR),
            cal.get(java.util.Calendar.MONTH) + 1,
            cal.get(java.util.Calendar.DAY_OF_MONTH)
        )
    }

    private fun applyDateFilter(type: String) {
        val today = getTodayString()
        filteredList.clear()

        when (type) {
            "TODAY" -> filteredList.addAll(appointmentList.filter { it.date == today })
            "UPCOMING" -> filteredList.addAll(appointmentList.filter { it.date > today })
            "ALL" -> filteredList.addAll(appointmentList)
        }

        appointmentAdapter.submitList(filteredList.toList())
        toggleEmptyState(filteredList.isEmpty())

        SuperToast.show(
            this,
            SuperToast.Type.SUCCESS,
            "Filter Applied",
            "Showing $type appointments"
        )
    }

    private fun setupSearchFilter() {
        binding.etSearchDoctor.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                applySearch(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun applySearch(query: String) {
        val q = query.lowercase()
        filteredList.clear()

        filteredList.addAll(
            if (q.isEmpty()) appointmentList
            else appointmentList.filter {
                it.userName.lowercase().contains(q) ||
                        it.date.lowercase().contains(q) ||
                        it.time.lowercase().contains(q)
            }
        )

        appointmentAdapter.submitList(filteredList.toList())
        toggleEmptyState(filteredList.isEmpty())
    }

    private fun setupDrawer() {
        binding.ivDoctorProfile.setOnClickListener {
            binding.doctorDrawerLayout.openDrawer(GravityCompat.START)
        }

        val headerView = binding.doctorNavigationView.getHeaderView(0)
        val headerName = headerView.findViewById<TextView>(R.id.headerDoctorName)
        val headerImage = headerView.findViewById<ImageView>(R.id.headerDoctorImage)

        val uid = auth.currentUser?.uid ?: return

        dbRefDoctors.child(uid).child("name").get()
            .addOnSuccessListener { headerName.text = "Dr. ${it.value}" }

        auth.currentUser?.photoUrl?.let {
            Glide.with(this)
                .load(it)
                .circleCrop()
                .into(headerImage)
        }

        binding.doctorNavigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> binding.doctorDrawerLayout.closeDrawer(GravityCompat.START)

                R.id.nav_profile -> startActivity(Intent(this, DoctorProfileActivity::class.java))

                R.id.nav_appointments ->
                    startActivity(Intent(this, DoctorApprovedListActivity::class.java))

                R.id.nav_logout -> {
                    FirebaseAuth.getInstance().signOut()

                    val i = Intent(this, RoleSelectActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(i)

                    SuperToast.show(
                        this,
                        SuperToast.Type.WARNING,
                        "Logged Out",
                        "See you again!"
                    )
                }
            }
            true
        }
    }

    private fun loadDoctorName() {
        val uid = auth.currentUser?.uid ?: return

        dbRefDoctors.child(uid).get()
            .addOnSuccessListener { snapshot ->
                val name = snapshot.child("name").value?.toString() ?: "Doctor"
                binding.tvDoctorName.text = "Dr. $name"

                val headerView = binding.doctorNavigationView.getHeaderView(0)
                val hName = headerView.findViewById<TextView>(R.id.headerDoctorName)
                hName.text = "Dr. $name"

                val imageUrl = snapshot.child("imageUrl").value?.toString()
                if (!imageUrl.isNullOrEmpty()) {
                    val hImg = headerView.findViewById<ImageView>(R.id.headerDoctorImage)
                    Glide.with(this).load(imageUrl).circleCrop().into(hImg)
                }
            }
    }

    private fun setupLottieSlider() {
        val slides = listOf(
            SlideItem(R.raw.doctor_slide_1, "Manage Appointments Easily."),
            SlideItem(R.raw.doctor_slide_2, "Smart Dashboard for Smart Doctors."),
            SlideItem(R.raw.doctor_slide_3, "Update Profile Anytime.")
        )

        sliderAdapter = LottieSliderAdapter(slides)
        binding.lottieSlider.adapter = sliderAdapter

        binding.lottieSlider.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        sliderHandler.postDelayed(sliderRunnable, 3000)
    }

    private val sliderRunnable = object : Runnable {
        override fun run() {
            if (::sliderAdapter.isInitialized) {
                if (currentPage >= sliderAdapter.itemCount) currentPage = 0
                binding.lottieSlider.setCurrentItem(currentPage++, true)
            }
            sliderHandler.postDelayed(this, 3000)
        }
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 3000)
    }

    private fun setupRecyclerView() {
        appointmentAdapter = AppointmentAdapter(
            onApprove = { a -> updateAppointmentStatusSafe(a, "Approved") },
            onReject = { a -> updateAppointmentStatusSafe(a, "Rejected") }
        )

        binding.rvAppointments.layoutManager = LinearLayoutManager(this)
        binding.rvAppointments.adapter = appointmentAdapter

        binding.rvAppointments.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(this, R.anim.recycler_fade_in)
    }

    private fun fetchAppointments() {
        val doctorId = auth.currentUser?.uid ?: return

        binding.progressBar.visibility = View.VISIBLE
        binding.emptyContainer.visibility = View.GONE

        appointmentListener?.let { dbRefAppointments.removeEventListener(it) }

        appointmentListener =
            dbRefAppointments.orderByChild("doctorId").equalTo(doctorId)
                .addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        appointmentList.clear()
                        filteredList.clear()

                        for (child in snapshot.children) {
                            val appt = child.getValue(Appointment::class.java)
                                ?.copy(appointmentId = child.key ?: "")
                            if (appt != null && appt.status == "Pending") {
                                appointmentList.add(appt)
                            }
                        }

                        filteredList.addAll(appointmentList)

                        binding.progressBar.visibility = View.GONE
                        toggleEmptyState(filteredList.isEmpty())

                        appointmentAdapter.submitList(filteredList.toList())
                        binding.rvAppointments.scheduleLayoutAnimation()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        binding.progressBar.visibility = View.GONE
                        SuperToast.show(
                            this@DoctorDashboardActivity,
                            SuperToast.Type.ERROR,
                            "Error",
                            error.message
                        )
                    }
                })
    }

    private fun toggleEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            binding.rvAppointments.visibility = View.GONE
            binding.emptyContainer.visibility = View.VISIBLE
            binding.lottieEmpty.playAnimation()
            animateEmpty()
        } else {
            binding.emptyContainer.visibility = View.GONE
            binding.lottieEmpty.cancelAnimation()
            binding.rvAppointments.visibility = View.VISIBLE
        }
    }

    private fun updateAppointmentStatusSafe(app: Appointment?, status: String) {
        if (app == null) {
            SuperToast.show(
                this,
                SuperToast.Type.ERROR,
                "Invalid Appointment",
                "Data not found"
            )
            return
        }
        updateAppointmentStatus(app, status)
    }

    private fun updateAppointmentStatus(appt: Appointment, status: String) {
        val id = appt.appointmentId

        if (id.isBlank()) {
            SuperToast.show(
                this,
                SuperToast.Type.ERROR,
                "Error",
                "Invalid appointment ID"
            )
            return
        }

        dbRefAppointments.child(id).child("status").setValue(status)
            .addOnSuccessListener {
                SuperToast.show(
                    this,
                    SuperToast.Type.SUCCESS,
                    "Updated",
                    "Marked as $status"
                )
            }
            .addOnFailureListener {
                SuperToast.show(
                    this,
                    SuperToast.Type.ERROR,
                    "Failed",
                    it.message ?: "Something went wrong"
                )
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        appointmentListener?.let { dbRefAppointments.removeEventListener(it) }
        sliderHandler.removeCallbacks(sliderRunnable)
        binding.lottieEmpty.cancelAnimation()
    }

    private fun animateHeader() {
        binding.headerDoctor.translationY = -80f
        binding.headerDoctor.alpha = 0f

        binding.headerDoctor.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(650)
            .setStartDelay(150)
            .start()
    }

    private fun animateSlider() {
        binding.lottieSlider.scaleX = 0.85f
        binding.lottieSlider.scaleY = 0.85f
        binding.lottieSlider.alpha = 0f

        binding.lottieSlider.animate()
            .scaleX(1f)
            .scaleY(1f)
            .alpha(1f)
            .setDuration(650)
            .setStartDelay(250)
            .start()
    }

    private fun animateQuickActions() {
        binding.layoutActions.translationY = 50f
        binding.layoutActions.alpha = 0f

        binding.layoutActions.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(650)
            .setStartDelay(350)
            .start()
    }

    private fun animateSearchBar() {
        binding.layoutSearchBarDoctor.translationY = 40f
        binding.layoutSearchBarDoctor.alpha = 0f

        binding.layoutSearchBarDoctor.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(550)
            .setStartDelay(300)
            .start()
    }

    private fun animateEmpty() {
        binding.emptyContainer.scaleX = 0.8f
        binding.emptyContainer.scaleY = 0.8f
        binding.emptyContainer.alpha = 0f

        binding.emptyContainer.animate()
            .scaleX(1f)
            .scaleY(1f)
            .alpha(1f)
            .setDuration(400)
            .start()
    }
}
