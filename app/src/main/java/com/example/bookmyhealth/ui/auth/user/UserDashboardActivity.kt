package com.example.bookmyhealth.ui.auth.user

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.bookmyhealth.R
import com.example.bookmyhealth.adapter.DoctorListAdapter
import com.example.bookmyhealth.adapter.UserLottieAdapter
import com.example.bookmyhealth.data.model.Doctor
import com.example.bookmyhealth.data.model.UserSlideItem
import com.example.bookmyhealth.databinding.ActivityUserDashboardBinding
import com.example.bookmyhealth.ui.auth.RoleSelectActivity
import com.example.bookmyhealth.ui.profile.UserProfileActivity
import com.example.bookmyhealth.utils.SuperToast
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UserDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDashboardBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var userRef: DatabaseReference
    private lateinit var doctorRef: DatabaseReference

    private lateinit var adapter: DoctorListAdapter
    private val doctorList = mutableListOf<Doctor>()
    private var filteredList = mutableListOf<Doctor>()

    private lateinit var sliderAdapter: UserLottieAdapter
    private val sliderHandler = Handler(Looper.getMainLooper())
    private var currentPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.ivProfile.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        setupDrawerClicks()

        val uid = auth.currentUser?.uid ?: ""
        if (uid.isEmpty()) {
            SuperToast.show(
                this,
                SuperToast.Type.ERROR,
                "Not Logged In",
                "Please login again"
            )
            startActivity(Intent(this, RoleSelectActivity::class.java))
            finish()
            return
        }

        // ✅ SHOW DIALOG IF COMING FROM SIGNUP (GOOGLE OR MANUAL)
        if (intent.getBooleanExtra(UserSignupActivity.EXTRA_FROM_SIGNUP, false)) {
            showWelcomeDialog()
        }

        val firebaseUrl =
            "https://bookmyhealth-5920a-default-rtdb.asia-southeast1.firebasedatabase.app/"
        val database = FirebaseDatabase.getInstance(firebaseUrl)

        userRef = database.getReference("users").child(uid)
        doctorRef = database.getReference("doctors")

        fetchUserName()
        fetchUserProfileImage()

        setupRecyclerView()
        setupClicks()
        setupUserSlider()
        setupSearch()
        setupRefresh()
        setupFilter()
        fetchAllDoctors()
    }


    // ✅ FULLY FIXED WELCOME ALERT DIALOG WITH BOTH BUTTONS
    private fun showWelcomeDialog() {
        val dialogView = layoutInflater.inflate(R.layout.alert_success, null)

        val dialog = android.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val btnOk = dialogView.findViewById<View>(R.id.btnDialogOk)
        val btnGoProfile = dialogView.findViewById<View>(R.id.btnGoProfile)

        btnOk.setOnClickListener {
            dialog.dismiss()
        }

        btnGoProfile.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this, UserProfileActivity::class.java))
        }

        dialog.show()
    }


    // -------------------------------------------------------------------
    private fun setupDrawerClicks() {
        binding.sideNav.setNavigationItemSelectedListener { item ->
            when (item.itemId) {

                R.id.nav_home -> binding.drawerLayout.closeDrawer(GravityCompat.START)

                R.id.nav_profile -> {
                    startActivity(Intent(this, UserProfileActivity::class.java))
                }

                R.id.nav_appointments -> {
                    startActivity(Intent(this, UserAppointmentsActivity::class.java))
                }

                R.id.nav_logout -> {
                    auth.signOut()
                    SuperToast.show(
                        this,
                        SuperToast.Type.SUCCESS,
                        "Logged Out",
                        "You have been logged out"
                    )
                    startActivity(Intent(this, RoleSelectActivity::class.java))
                    finish()
                }
            }
            true
        }
    }

    // -------------------------------------------------------------------
    private fun fetchUserName() {

        val headerView = binding.sideNav.getHeaderView(0)
        val drawerUserName = headerView.findViewById<TextView>(R.id.drawerUserName)
        val drawerUserEmail = headerView.findViewById<TextView>(R.id.drawerUserEmail)

        userRef.get().addOnSuccessListener { snapshot ->

            val name = snapshot.child("name").value?.toString() ?: "User"
            val email = snapshot.child("email").value?.toString()
                ?: auth.currentUser?.email ?: "No Email"

            binding.tvWelcome.text = "Welcome, $name 👋"

            drawerUserName.text = name
            drawerUserEmail.text = email

        }.addOnFailureListener {
            binding.tvWelcome.text = "Welcome, User 👋"
        }
    }

    // -------------------------------------------------------------------
    private fun fetchUserProfileImage() {

        val headerView = binding.sideNav.getHeaderView(0)
        val drawerProfileImage =
            headerView.findViewById<android.widget.ImageView>(R.id.drawerUserImage)

        userRef.child("imageUrl").get().addOnSuccessListener { snap ->

            val imageUrl = snap.value?.toString() ?: auth.currentUser?.photoUrl?.toString()

            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.profile_placeholder)
                .circleCrop()
                .into(binding.ivProfile)

            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.profile_placeholder)
                .circleCrop()
                .into(drawerProfileImage)
        }
    }

    // -------------------------------------------------------------------
    private fun setupUserSlider() {

        val slides = listOf(
            UserSlideItem(R.raw.user_slide_1, "Book Appointments Easily"),
            UserSlideItem(R.raw.user_slide_2, "Find the Best Doctors Near You"),
            UserSlideItem(R.raw.user_slide_3, "Your Health, Our Priority")
        )

        sliderAdapter = UserLottieAdapter(slides)

        binding.userSlider.adapter = sliderAdapter
        binding.userSlider.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.userSlider.setPageTransformer { page, position ->
            val scale = 0.90f + (1 - kotlin.math.abs(position)) * 0.10f
            page.scaleY = scale
        }

        startAutoSlider()
    }

    private fun startAutoSlider() {
        sliderHandler.postDelayed(sliderRunnable, 3000)
    }

    private val sliderRunnable = object : Runnable {
        override fun run() {
            if (::sliderAdapter.isInitialized) {
                val count = sliderAdapter.itemCount
                if (count > 0) {
                    if (currentPage >= count) currentPage = 0
                    binding.userSlider.setCurrentItem(currentPage++, true)
                }
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
        startAutoSlider()
    }

    // -------------------------------------------------------------------
    private fun setupClicks() {
        binding.cardAppointments.setOnClickListener {
            startActivity(Intent(this, UserAppointmentsActivity::class.java))
        }

        binding.cardProfile.setOnClickListener {
            startActivity(Intent(this, UserProfileActivity::class.java))
        }
    }

    // -------------------------------------------------------------------
    private fun setupRecyclerView() {
        adapter = DoctorListAdapter { doctor ->
            val intent = Intent(this, BookAppointmentActivity::class.java)
            intent.putExtra("doctorId", doctor.uid)
            intent.putExtra("doctorName", doctor.name)
            startActivity(intent)
        }

        binding.recyclerDoctors.layoutManager = LinearLayoutManager(this)
        binding.recyclerDoctors.adapter = adapter
    }

    // -------------------------------------------------------------------
    private fun fetchAllDoctors() {
        showLoading(true)

        doctorRef.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                doctorList.clear()

                if (snapshot.exists()) {
                    snapshot.children.forEach { child ->
                        val doctor = child.getValue(Doctor::class.java)
                        if (doctor != null) {
                            doctor.uid = child.key ?: ""
                            doctorList.add(doctor)
                        }
                    }
                }

                adapter.submitList(doctorList.toList())
                showLoading(false)
                showEmptyState(doctorList.isEmpty())
            }

            override fun onCancelled(error: DatabaseError) {
                showLoading(false)
                SuperToast.show(
                    this@UserDashboardActivity,
                    SuperToast.Type.ERROR,
                    "Failed",
                    error.message
                )
            }
        })
    }

    // -------------------------------------------------------------------
    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterDoctors(s.toString())
            }
        })
    }

    private fun filterDoctors(query: String) {
        if (query.isEmpty()) {
            adapter.submitList(doctorList.toList())
            showEmptyState(doctorList.isEmpty())
            return
        }

        filteredList = doctorList.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.specialization.contains(query, ignoreCase = true)
        }.toMutableList()

        adapter.submitList(filteredList)
        showEmptyState(filteredList.isEmpty())
    }

    // -------------------------------------------------------------------
    private fun setupRefresh() {
        binding.btnRefresh.setOnClickListener {
            SuperToast.show(
                this,
                SuperToast.Type.SUCCESS,
                "Refreshing…",
                "Updating doctor list"
            )
            fetchAllDoctors()
            binding.recyclerDoctors.smoothScrollToPosition(0)
        }
    }

    // -------------------------------------------------------------------
    private fun setupFilter() {
        binding.btnFilter.setOnClickListener {
            openFilterDialog()
        }
    }

    private fun openFilterDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_filter_user, null)

        val dialog = android.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val chipGroup = dialogView.findViewById<com.google.android.material.chip.ChipGroup>(R.id.chipGroupSpeciality)
        val btnApply = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnApply)
        val btnClear = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnClear)

        chipGroup.removeAllViews()
        val specialities = doctorList.map { it.specialization }.distinct()

        specialities.forEach { specialty ->
            val chip = Chip(this)
            chip.text = specialty
            chip.isCheckable = true
            chip.chipBackgroundColor = getColorStateList(R.color.chip_selector)
            chip.setTextColor(getColor(R.color.black))
            chip.chipCornerRadius = 24f
            chipGroup.addView(chip)
        }

        btnClear.setOnClickListener {
            adapter.submitList(doctorList.toList())
            showEmptyState(doctorList.isEmpty())
            dialog.dismiss()
        }

        btnApply.setOnClickListener {
            val selectedChipId = chipGroup.checkedChipId

            if (selectedChipId == -1) {
                SuperToast.show(
                    this,
                    SuperToast.Type.WARNING,
                    "No Selection",
                    "Please select a speciality!"
                )
                return@setOnClickListener
            }

            val chip = chipGroup.findViewById<Chip>(selectedChipId)
            val selectedSpeciality = chip.text.toString()

            filteredList = doctorList.filter {
                it.specialization.equals(selectedSpeciality, true)
            }.toMutableList()

            adapter.submitList(filteredList)
            showEmptyState(filteredList.isEmpty())

            dialog.dismiss()
        }

        dialog.show()
    }

    // -------------------------------------------------------------------
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.recyclerDoctors.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.emptyContainer.visibility = View.GONE
    }

    private fun showEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            binding.recyclerDoctors.visibility = View.GONE
            binding.emptyContainer.visibility = View.VISIBLE
            binding.lottieEmpty.playAnimation()
        } else {
            binding.emptyContainer.visibility = View.GONE
            binding.lottieEmpty.cancelAnimation()
            binding.recyclerDoctors.visibility = View.VISIBLE
        }
    }
}
