package com.example.bookmyhealth.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.bookmyhealth.R
import com.example.bookmyhealth.databinding.DialogBookingConfirmBinding
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class BookingConfirmDialog : DialogFragment() {

    private var _binding: DialogBookingConfirmBinding? = null
    private val binding get() = _binding!!

    private var doctorName: String = ""
    private var appointmentDate: Date? = null
    private var appointmentTime: String = ""
    private var slotNumber: Int = 0
    private var tokenNumber: String = ""
    private var refId: String = ""
    private var onDismissListener: (() -> Unit)? = null

    private val db by lazy {
        FirebaseDatabase.getInstance(
            "https://bookmyhealth-5920a-default-rtdb.asia-southeast1.firebasedatabase.app"
        ).reference
    }

    companion object {
        private const val ARG_DOCTOR_NAME = "doctor_name"
        private const val ARG_DATE = "date"
        private const val ARG_TIME = "time"
        private const val ARG_SLOT = "slot"
        private const val ARG_TOKEN = "token"
        private const val ARG_REF_ID = "ref_id"

        fun newInstance(
            doctorName: String,
            date: Date,
            time: String,
            slot: Int,
            tokenNumber: String,
            refId: String
        ): BookingConfirmDialog {
            return BookingConfirmDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_DOCTOR_NAME, doctorName)
                    putLong(ARG_DATE, date.time)
                    putString(ARG_TIME, time)
                    putInt(ARG_SLOT, slot)
                    putString(ARG_TOKEN, tokenNumber)
                    putString(ARG_REF_ID, refId)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Material_Light_NoActionBar)

        arguments?.let {
            doctorName = it.getString(ARG_DOCTOR_NAME, "")
            appointmentDate = Date(it.getLong(ARG_DATE, System.currentTimeMillis()))
            appointmentTime = it.getString(ARG_TIME, "")
            slotNumber = it.getInt(ARG_SLOT, 0)
            tokenNumber = it.getString(ARG_TOKEN, "")
            refId = it.getString(ARG_REF_ID, "")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogBookingConfirmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupData()
        fetchStatusFromFirebase()  // 🔥 FETCH STATUS FROM FIREBASE
        setupClickListeners()
    }

    private fun setupData() {
        binding.tvBillDoctor.text = doctorName

        appointmentDate?.let { date ->
            val dateFormat = SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.getDefault())
            binding.tvBillDate.text = dateFormat.format(date)
        }

        binding.tvBillTime.text = appointmentTime
        binding.tvBillSlot.text = "Slot #$slotNumber"
        binding.tvBillToken.text = tokenNumber

        val shortRefId = refId.take(8).uppercase()
        binding.tvBillAppId.text = "BMH$shortRefId"
    }

    // 🔥 FETCH STATUS FROM FIREBASE
    private fun fetchStatusFromFirebase() {
        db.child("appointments").child(refId).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val status = snapshot.child("status").value?.toString() ?: "Pending"
                    updateStatusUI(status)
                } else {
                    updateStatusUI("Pending")
                }
            }
            .addOnFailureListener {
                updateStatusUI("Pending")
            }
    }

    // 🔥 UPDATE STATUS UI BASED ON FIREBASE VALUE
    private fun updateStatusUI(status: String) {
        binding.tvBillStatus.text = status

        when (status.lowercase()) {
            "confirmed" -> {
                binding.tvBillStatus.text = "Confirmed"
                binding.tvBillStatus.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.success)
                )
                binding.tvBillStatus.setBackgroundResource(R.drawable.bg_status_confirmed)
            }
            "pending" -> {
                binding.tvBillStatus.text = "Pending"
                binding.tvBillStatus.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.warning)
                )
                binding.tvBillStatus.setBackgroundResource(R.drawable.bg_status_pending)
            }
            "cancelled" -> {
                binding.tvBillStatus.text = "Cancelled"
                binding.tvBillStatus.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.error)
                )
                binding.tvBillStatus.setBackgroundResource(R.drawable.bg_status_cancelled)
            }
            "completed" -> {
                binding.tvBillStatus.text = "Completed"
                binding.tvBillStatus.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.primary)
                )
                binding.tvBillStatus.setBackgroundResource(R.drawable.bg_status_completed)
            }
            else -> {
                binding.tvBillStatus.text = status
                binding.tvBillStatus.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.text_secondary)
                )
                binding.tvBillStatus.setBackgroundResource(R.drawable.bg_status_pending)
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnBillDone.setOnClickListener {
            dismiss()
        }
    }

    fun setOnDismissListener(listener: () -> Unit) {
        onDismissListener = listener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onDismissListener?.invoke()
        _binding = null
    }
}