package com.example.bookmyhealth.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmyhealth.data.model.Appointment
import com.example.bookmyhealth.databinding.ItemUserAppointmentBinding

class UserAppointmentAdapter :
    ListAdapter<Appointment, UserAppointmentAdapter.ViewHolder>(AppointmentDiffCallback()) {

    inner class ViewHolder(private val binding: ItemUserAppointmentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(appointment: Appointment) = with(binding) {
            // ✅ Null-safe + readable UI text
            tvDoctorId.text = "👨‍⚕️ Doctor: ${appointment.doctorName ?: "N/A"}"
            tvDate.text = "📅 Date: ${appointment.date ?: "N/A"}"
            tvTime.text = "🕒 Time: ${appointment.time ?: "N/A"}"
            tvStatus.text = "Status: ${appointment.status ?: "Pending"}"

            // ✅ Status color logic
            val statusColor = when (appointment.status?.lowercase()) {
                "approved" -> Color.parseColor("#4CAF50") // green
                "rejected" -> Color.parseColor("#F44336") // red
                else -> Color.parseColor("#FF9800")       // orange (pending)
            }
            tvStatus.setTextColor(statusColor)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserAppointmentBinding.inflate(
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

/** ✅ Efficient DiffUtil for smooth list updates without flicker */
class AppointmentDiffCallback : DiffUtil.ItemCallback<Appointment>() {
    override fun areItemsTheSame(oldItem: Appointment, newItem: Appointment): Boolean {
        return oldItem.appointmentId == newItem.appointmentId
    }

    override fun areContentsTheSame(oldItem: Appointment, newItem: Appointment): Boolean {
        return oldItem == newItem
    }
}
