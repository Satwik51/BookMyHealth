package com.example.bookmyhealth.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmyhealth.R
import com.example.bookmyhealth.data.model.IntroItem

class IntroAdapter(private val items: List<IntroItem>) :
    RecyclerView.Adapter<IntroAdapter.IntroViewHolder>() {

    inner class IntroViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivIcon: ImageView = view.findViewById(R.id.ivIcon)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvDescription: TextView = view.findViewById(R.id.tvDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_intro_slide, parent, false)
        return IntroViewHolder(view)
    }

    override fun onBindViewHolder(holder: IntroViewHolder, position: Int) {
        val item = items[position]
        holder.ivIcon.setImageResource(item.icon)
        holder.tvTitle.text = item.title
        holder.tvDescription.text = item.description
    }

    override fun getItemCount(): Int = items.size
}
