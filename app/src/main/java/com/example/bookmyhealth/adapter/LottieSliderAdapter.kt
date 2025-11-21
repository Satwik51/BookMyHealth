package com.example.bookmyhealth.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.bookmyhealth.R
import com.example.bookmyhealth.data.model.SlideItem


class LottieSliderAdapter(
    private val slides: List<SlideItem>
) : RecyclerView.Adapter<LottieSliderAdapter.LottieViewHolder>() {

    inner class LottieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lottie: LottieAnimationView = itemView.findViewById(R.id.lottieView)
        val slideText: TextView = itemView.findViewById(R.id.tvSlideText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LottieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lottie_slide, parent, false)
        return LottieViewHolder(view)
    }

    override fun onBindViewHolder(holder: LottieViewHolder, position: Int) {
        val item = slides[position]

        // Set animation
        holder.lottie.setAnimation(item.animation)
        holder.lottie.playAnimation()

        // Set text for each slide
        holder.slideText.text = item.text
    }

    override fun getItemCount(): Int = slides.size
}
