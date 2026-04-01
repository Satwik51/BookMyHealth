package com.example.bookmyhealth.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmyhealth.R
import com.example.bookmyhealth.data.model.ChatMessage
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter(
    private val list: MutableList<ChatMessage>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_USER = 1
        const val TYPE_AI = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].isUser) TYPE_USER else TYPE_AI
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == TYPE_USER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat_user, parent, false)
            UserViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat_ai, parent, false)
            AiViewHolder(view)
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val message = list[position]

        when (holder) {
            is UserViewHolder -> holder.bind(message)
            is AiViewHolder -> holder.bind(message)
        }

        // 🔥 Animate only new items (fix flicker)
        if (position == list.size - 1) {
            animateItem(holder.itemView)
        }
    }

    // 🔥 SMOOTH ENTRY ANIMATION
    private fun animateItem(view: View) {
        view.translationY = 40f
        view.alpha = 0f

        view.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(200)
            .setInterpolator(DecelerateInterpolator())
            .start()
    }

    // 📅 FORMAT TIME FROM MODEL
    private fun formatTime(time: Long): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(Date(time))
    }

    // 👤 USER VIEW HOLDER
    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
        private val tvTime: TextView = itemView.findViewById(R.id.tvTime)

        fun bind(msg: ChatMessage) {
            tvMessage.text = msg.message
            tvTime.text = formatTime(msg.time) // ✅ FIXED
        }
    }

    // 🤖 AI VIEW HOLDER
    inner class AiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
        private val tvTime: TextView = itemView.findViewById(R.id.tvTime)

        fun bind(msg: ChatMessage) {
            tvMessage.text = msg.message
            tvTime.text = formatTime(msg.time) // ✅ FIXED
        }
    }

    // 🔥 BETTER UPDATE METHOD (USE THIS)
    fun updateMessages(newList: List<ChatMessage>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    // ➕ ADD SINGLE MESSAGE (BEST FOR PERFORMANCE)
    fun addMessage(message: ChatMessage) {
        list.add(message)
        notifyItemInserted(list.size - 1)
    }
}