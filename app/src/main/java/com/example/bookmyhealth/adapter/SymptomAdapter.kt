package com.example.bookmyhealth.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmyhealth.R
import com.example.bookmyhealth.data.model.Symptom
import com.example.bookmyhealth.databinding.ItemSymptomBinding

class SymptomAdapter(
    private val onSymptomClick: (Symptom) -> Unit
) : ListAdapter<Symptom, SymptomAdapter.ViewHolder>(SymptomDiffCallback()) {

    inner class ViewHolder(val binding: ItemSymptomBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSymptomBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val symptom = getItem(position)

        holder.binding.tvSymptom.text = symptom.name

        if (symptom.isSelected) {
            holder.binding.cardSymptom.setCardBackgroundColor(
                holder.itemView.context.getColor(R.color.primary)
            )
            holder.binding.tvSymptom.setTextColor(Color.WHITE)
            holder.binding.ivCheck.visibility = android.view.View.VISIBLE
        } else {
            holder.binding.cardSymptom.setCardBackgroundColor(
                holder.itemView.context.getColor(R.color.white)
            )
            holder.binding.tvSymptom.setTextColor(
                holder.itemView.context.getColor(R.color.text_primary)
            )
            holder.binding.ivCheck.visibility = android.view.View.GONE
        }

        holder.binding.root.setOnClickListener {
            onSymptomClick(symptom)
        }
    }

    class SymptomDiffCallback : DiffUtil.ItemCallback<Symptom>() {
        override fun areItemsTheSame(oldItem: Symptom, newItem: Symptom): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Symptom, newItem: Symptom): Boolean {
            return oldItem == newItem
        }
    }
}