package com.example.bookmyhealth.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookmyhealth.R
import com.example.bookmyhealth.data.model.Doctor
import com.example.bookmyhealth.databinding.ItemDoctorBinding

class DoctorListAdapter(
    private val onDoctorClick: (Doctor) -> Unit
) : ListAdapter<Doctor, DoctorListAdapter.DoctorViewHolder>(DoctorDiffCallback()) {

    inner class DoctorViewHolder(private val binding: ItemDoctorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(doctor: Doctor) = with(binding) {

            // ⭐ Doctor Name
            tvDoctorName.text = if (doctor.name.isNotEmpty())
                "Dr. ${doctor.name}"
            else
                "Doctor"

            // ⭐ Specialization
            tvSpecialization.text =
                doctor.specialization.ifEmpty { "Specialization not available" }

            // ⭐ Experience
            tvExperience.text = if (doctor.experience.isNotEmpty())
                "${doctor.experience} yrs experience"
            else
                "Experience: N/A"

            // ⭐ Clinic Name (added)
            tvClinicName?.text = doctor.clinicName.ifEmpty { "Clinic not specified" }

            // ⭐ Achievements (optional)
            tvAchievements?.apply {
                if (doctor.achievements.isNotEmpty()) {
                    visibility = View.VISIBLE
                    text = doctor.achievements
                } else visibility = View.GONE
            }

            // ⭐ Weekly Availability (Mon, Tue... converted to text)
            tvAvailability?.apply {
                if (doctor.weeklyAvailability.isNotEmpty()) {
                    visibility = View.VISIBLE
                    text = doctor.weeklyAvailability.joinToString(", ")
                } else visibility = View.GONE
            }

            // ⭐ Load Profile Image Safely
            Glide.with(imgDoctor.context)
                .load(
                    if (doctor.imageUrl.isNotEmpty())
                        doctor.imageUrl
                    else R.drawable.ic_doctor_placeholder
                )
                .placeholder(R.drawable.ic_doctor_placeholder)
                .error(R.drawable.ic_doctor_placeholder)
                .into(imgDoctor)

            // ⭐ Click Action
            root.setOnClickListener { onDoctorClick(doctor) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        return DoctorViewHolder(
            ItemDoctorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

/** ⭐ Efficient DiffUtil — Best for large doctors list */
class DoctorDiffCallback : DiffUtil.ItemCallback<Doctor>() {
    override fun areItemsTheSame(oldItem: Doctor, newItem: Doctor): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: Doctor, newItem: Doctor): Boolean {
        return oldItem == newItem
    }
}
