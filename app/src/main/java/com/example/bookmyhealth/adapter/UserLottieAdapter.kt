package com.example.bookmyhealth.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.bookmyhealth.R
import com.example.bookmyhealth.data.model.UserSlideItem

class UserLottieAdapter(
    private val slides: List<UserSlideItem>
) : RecyclerView.Adapter<UserLottieAdapter.SliderViewHolder>() {

    inner class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val anim: LottieAnimationView = itemView.findViewById(R.id.lottieAnim)
        val text: TextView = itemView.findViewById(R.id.tvSlideText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_slide, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val item = slides[position]

        // ⭐ Set Animation
        holder.anim.setAnimation(item.animation)
        holder.anim.speed = 1.0f
        holder.anim.playAnimation()

        // ⭐ Set text
        holder.text.text = item.text
    }

    override fun getItemCount(): Int = slides.size
}
