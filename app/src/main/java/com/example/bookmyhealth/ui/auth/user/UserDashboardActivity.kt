package com.example.bookmyhealth.ui.auth.user

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.bookmyhealth.R
import com.example.bookmyhealth.adapter.DoctorListAdapter
import com.example.bookmyhealth.adapter.PosterAdapter
import com.example.bookmyhealth.adapter.UserLottieAdapter
import com.example.bookmyhealth.data.model.Doctor
import com.example.bookmyhealth.data.model.UserSlideItem
import com.example.bookmyhealth.databinding.ActivityUserDashboardBinding
import com.example.bookmyhealth.ui.auth.RoleSelectActivity
import com.example.bookmyhealth.ui.profile.UserProfileActivity
import com.example.bookmyhealth.ui.symptom.SymptomCheckerActivity
import com.example.bookmyhealth.ui.drug.DrugSearchActivity
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
    private var isFabOpen = false

    private var currentFilter = ""

    private lateinit var sliderAdapter: UserLottieAdapter
    private val sliderHandler = Handler(Looper.getMainLooper())
    private var currentPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPosterSlider()

        // 🔥 FAB MAIN BUTTON
        binding.fabSymptomChecker.setOnClickListener {
            if (isFabOpen) {
                binding.fabMenuLayout.visibility = View.GONE
                isFabOpen = false
            } else {
                binding.fabMenuLayout.visibility = View.VISIBLE
                isFabOpen = true
            }
        }

        val btnPrescription = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnComingSoon2)

        btnPrescription.setOnClickListener {
            startActivity(
                Intent(this, com.example.bookmyhealth.ui.chat.ChatActivity::class.java)
            )
        }

        // ✅ Symptom Checker
        binding.btnSymptomChecker.setOnClickListener {
            binding.fabMenuLayout.visibility = View.GONE
            isFabOpen = false
            startActivity(Intent(this, SymptomCheckerActivity::class.java))
        }

        // 🔥 NEW: Drug Search
        binding.btnDrugSearch.setOnClickListener {
            binding.fabMenuLayout.visibility = View.GONE
            isFabOpen = false
            startActivity(Intent(this, DrugSearchActivity::class.java))
        }

        // ---------------- AUTH ----------------
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid ?: ""

        if (uid.isEmpty()) {
            SuperToast.show(this, SuperToast.Type.ERROR, "Not Logged In", "Please login again")
            startActivity(Intent(this, RoleSelectActivity::class.java))
            finish()
            return
        }

        // ---------------- DRAWER ----------------
        binding.ivProfile.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        setupDrawerClicks()

        val firebaseUrl = "https://bookmyhealth-5920a-default-rtdb.asia-southeast1.firebasedatabase.app/"
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
    private lateinit var posterAdapter: PosterAdapter
    private val posterHandler = Handler(Looper.getMainLooper())
    private var posterPage = 0

    private fun setupPosterSlider() {

        val posters = listOf(
            R.drawable.banner1,
            R.drawable.banner2,
            R.drawable.banner3,
            R.drawable.banner4,
            R.drawable.banner5,
            R.drawable.banner6,
            R.drawable.banner7

        )

        posterAdapter = PosterAdapter(posters)
        binding.posterSlider.adapter = posterAdapter

        // auto slide
        posterHandler.postDelayed(object : Runnable {
            override fun run() {
                if (posterPage >= posters.size) posterPage = 0
                binding.posterSlider.setCurrentItem(posterPage++, true)
                posterHandler.postDelayed(this, 3000)
            }
        }, 3000)
    }

    // ---------------- DRAWER ----------------
    private fun setupDrawerClicks() {
        binding.sideNav.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> binding.drawerLayout.closeDrawer(GravityCompat.START)
                R.id.nav_profile -> startActivity(Intent(this, UserProfileActivity::class.java))
                R.id.nav_appointments -> startActivity(Intent(this, UserAppointmentsActivity::class.java))
                R.id.nav_logout -> {
                    auth.signOut()
                    SuperToast.show(this, SuperToast.Type.SUCCESS, "Logged Out", "You have been logged out")
                    startActivity(Intent(this, RoleSelectActivity::class.java))
                    finish()
                }
            }
            true
        }
    }

    // ---------------- USER DATA ----------------
    private fun fetchUserName() {
        val headerView = binding.sideNav.getHeaderView(0)
        val drawerUserName = headerView.findViewById<TextView>(R.id.drawerUserName)
        val drawerUserEmail = headerView.findViewById<TextView>(R.id.drawerUserEmail)

        userRef.get().addOnSuccessListener { snapshot ->
            val name = snapshot.child("name").value?.toString() ?: "User"
            val email = snapshot.child("email").value?.toString() ?: auth.currentUser?.email ?: "No Email"
            binding.tvWelcome.text = "Welcome, $name 👋"
            drawerUserName.text = name
            drawerUserEmail.text = email
        }
    }

    private fun fetchUserProfileImage() {
        val headerView = binding.sideNav.getHeaderView(0)
        val drawerProfileImage = headerView.findViewById<android.widget.ImageView>(R.id.drawerUserImage)

        userRef.child("imageUrl").get().addOnSuccessListener { snap ->
            val imageUrl = snap.value?.toString() ?: auth.currentUser?.photoUrl?.toString()
            Glide.with(this).load(imageUrl).placeholder(R.drawable.profile_placeholder).circleCrop().into(binding.ivProfile)
            Glide.with(this).load(imageUrl).placeholder(R.drawable.profile_placeholder).circleCrop().into(drawerProfileImage)
        }
    }

    // ---------------- SLIDER ----------------
    private fun setupUserSlider() {
        val slides = listOf(
            UserSlideItem(R.raw.user_slide_1, "Book Appointments Easily"),
            UserSlideItem(R.raw.user_slide_2, "Find the Best Doctors Near You"),
            UserSlideItem(R.raw.user_slide_3, "Your Health, Our Priority")
        )
        sliderAdapter = UserLottieAdapter(slides)
        binding.userSlider.adapter = sliderAdapter
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

    // ---------------- RECYCLER ----------------
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

    // ---------------- SEARCH ----------------
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
            return
        }
        filteredList = doctorList.filter {
            it.name.contains(query, true) || it.specialization.contains(query, true)
        }.toMutableList()
        adapter.submitList(filteredList)
    }

    // ⭐ FIXED: Multi-Appointment Logic
    private fun setupClicks() {
        binding.cardAppointments.setOnClickListener {
            startActivity(Intent(this, UserAppointmentsActivity::class.java))
        }

        binding.cardGetSlip.setOnClickListener {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@setOnClickListener
            val appointmentsRef = FirebaseDatabase.getInstance().getReference("appointments")

            // Yahan limitToLast(1) hata diya hai taaki sari check ho sakein
            appointmentsRef.orderByChild("userId").equalTo(uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var latestApprovedId: String? = null

                        // Loop through all results to find the most recent approved one
                        for (child in snapshot.children) {
                            val status = child.child("status").value?.toString() ?: ""
                            if (status == "Approved") {
                                latestApprovedId = child.key
                            }
                        }

                        if (latestApprovedId != null) {
                            val intent = Intent(this@UserDashboardActivity, AppointmentSlipActivity::class.java)
                            intent.putExtra("APPOINTMENT_ID", latestApprovedId)
                            startActivity(intent)
                        } else {
                            // Agar koi bhi approved nahi mili
                            SuperToast.show(this@UserDashboardActivity, SuperToast.Type.WARNING,
                                "No Approved Slip", "Koi bhi appointment abhi tak approve nahi hui hai.")
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        SuperToast.show(this@UserDashboardActivity, SuperToast.Type.ERROR, "Error", error.message)
                    }
                })
        }

        binding.cardProfile.setOnClickListener {
            startActivity(Intent(this, UserProfileActivity::class.java))
        }
    }

    private fun setupRefresh() {
        binding.btnRefresh.setOnClickListener {
            currentFilter = ""
            binding.etSearch.setText("")
            showBottomToast("Refreshing", "Fetching latest doctors...")
            fetchAllDoctors()
        }
    }

    private fun showBottomToast(title: String, message: String) {
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.custom_super_toast, null)
        val titleTv = view.findViewById<TextView>(R.id.toastTitle)
        val messageTv = view.findViewById<TextView>(R.id.toastMessage)
        titleTv.text = title
        messageTv.text = message
        val toast = android.widget.Toast(this)
        toast.duration = android.widget.Toast.LENGTH_SHORT
        toast.view = view
        toast.setGravity(android.view.Gravity.BOTTOM or android.view.Gravity.CENTER_HORIZONTAL, 0, 120)
        toast.show()
    }

    private fun applySearchAndFilter(query: String, filter: String) {
        val result = doctorList.filter {
            val search = it.name.contains(query, true) || it.specialization.contains(query, true)
            val spec = if (filter.isEmpty()) true else it.specialization.equals(filter, true)
            search && spec
        }
        adapter.submitList(result)
    }

    private fun setupFilter() {
        binding.btnFilter.setOnClickListener {
            val view = layoutInflater.inflate(R.layout.dialog_filter_user, null)
            val chipGroup = view.findViewById<com.google.android.material.chip.ChipGroup>(R.id.chipGroupSpeciality)
            val btnApply = view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnApply)
            val btnClear = view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnClear)

            val specs = doctorList.map { it.specialization }.distinct()
            specs.forEach {
                val chip = Chip(this)
                chip.text = it
                chip.isCheckable = true
                chipGroup.addView(chip)
            }

            var selected = ""
            chipGroup.setOnCheckedChangeListener { group, id ->
                val chip = group.findViewById<Chip>(id)
                selected = chip?.text?.toString() ?: ""
            }

            val dialog = AlertDialog.Builder(this).setView(view).create()
            btnApply.setOnClickListener {
                currentFilter = selected
                applySearchAndFilter(binding.etSearch.text.toString(), currentFilter)
                dialog.dismiss()
            }
            btnClear.setOnClickListener {
                chipGroup.clearCheck()
                selected = ""
            }
            dialog.show()
        }
    }

    private fun fetchAllDoctors() {
        doctorRef.get()
            .addOnSuccessListener { snapshot ->
                doctorList.clear()
                for (child in snapshot.children) {
                    val doctor = child.getValue(Doctor::class.java)
                    doctor?.let {
                        it.uid = child.key ?: ""
                        doctorList.add(it)
                    }
                }
                adapter.submitList(doctorList.toList())
                showBottomToast("Updated", "Doctors refreshed successfully")
            }
            .addOnFailureListener {
                showBottomToast("Error", "Failed to load doctors")
            }
    }
}