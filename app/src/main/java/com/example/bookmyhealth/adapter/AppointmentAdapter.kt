package com.example.bookmyhealth.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmyhealth.R
import com.example.bookmyhealth.data.model.Appointment
import com.example.bookmyhealth.databinding.ItemAppointmentBinding

class AppointmentAdapter(
    private val onApprove: (Appointment) -> Unit,
    private val onReject: (Appointment) -> Unit
) : ListAdapter<Appointment, AppointmentAdapter.ViewHolder>(AppointmentDiffCallback2()) {

    inner class ViewHolder(private val binding: ItemAppointmentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(appointment: Appointment) = with(binding) {

            // 👤 Patient Name
            tvUserName.text = "👤 Patient: ${appointment.userName.ifEmpty { "N/A" }}"

            // 📅 Date + Time
            tvDateTime.text =
                "📅 ${appointment.date.ifEmpty { "N/A" }}   🕒 ${appointment.time.ifEmpty { "N/A" }}"

            // 🔥 Status Chip
            val status = appointment.status.ifEmpty { "Pending" }
            tvStatus.text = status

            when (status.lowercase()) {
                "approved" -> {
                    tvStatus.setBackgroundResource(R.drawable.bg_status_chip_green)
                    tvStatus.setTextColor(Color.WHITE)
                }
                "rejected" -> {
                    tvStatus.setBackgroundResource(R.drawable.bg_status_chip_red)
                    tvStatus.setTextColor(Color.WHITE)
                }
                else -> {
                    tvStatus.setBackgroundResource(R.drawable.bg_status_chip_orange)
                    tvStatus.setTextColor(Color.WHITE)
                }
            }

            // 🔒 Disable buttons if not pending
            val isPending = status.equals("pending", ignoreCase = true)

            btnApprove.isEnabled = isPending
            btnReject.isEnabled = isPending

            btnApprove.alpha = if (isPending) 1f else 0.4f
            btnReject.alpha = if (isPending) 1f else 0.4f

            // 👉 Actions
            btnApprove.setOnClickListener { if (isPending) onApprove(appointment) }
            btnReject.setOnClickListener { if (isPending) onReject(appointment) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAppointmentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

/** ⚡ Unique DiffUtil (NO REDECLARATION ERROR) */
class AppointmentDiffCallback2 : DiffUtil.ItemCallback<Appointment>() {
    override fun areItemsTheSame(oldItem: Appointment, newItem: Appointment): Boolean {
        return oldItem.appointmentId == newItem.appointmentId
    }

    override fun areContentsTheSame(oldItem: Appointment, newItem: Appointment): Boolean {
        return oldItem == newItem
    }
}
