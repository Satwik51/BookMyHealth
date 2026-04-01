package com.example.bookmyhealth.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmyhealth.R
import com.example.bookmyhealth.data.model.AnalysisResult
import com.example.bookmyhealth.databinding.ItemResultBinding

class AnalysisResultAdapter : ListAdapter<AnalysisResult, AnalysisResultAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(val binding: ItemResultBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemResultBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = getItem(position)

        holder.binding.tvRank.text = "#${position + 1}"
        holder.binding.tvDiseaseName.text = result.diseaseName
        holder.binding.tvConfidence.text = "${result.confidence}%"
        holder.binding.progressConfidence.progress = result.confidence
        holder.binding.tvDoctorType.text = "Consult: ${result.doctorType}"
        holder.binding.tvMatchedSymptoms.text = "Matched: ${result.matchedSymptoms.take(3).joinToString(", ")}"

        // Color based on confidence
        val colorRes = when {
            result.confidence >= 70 -> R.color.accent_green
            result.confidence >= 50 -> R.color.accent_orange
            else -> R.color.accent_red
        }
        holder.binding.tvConfidence.setTextColor(
            holder.itemView.context.getColor(colorRes)
        )
        holder.binding.progressConfidence.setIndicatorColor(
            holder.itemView.context.getColor(colorRes)
        )
    }

    class DiffCallback : DiffUtil.ItemCallback<AnalysisResult>() {
        override fun areItemsTheSame(oldItem: AnalysisResult, newItem: AnalysisResult): Boolean {
            return oldItem.diseaseName == newItem.diseaseName
        }

        override fun areContentsTheSame(oldItem: AnalysisResult, newItem: AnalysisResult): Boolean {
            return oldItem == newItem
        }
    }
}