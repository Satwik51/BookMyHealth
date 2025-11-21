package com.example.bookmyhealth.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.bookmyhealth.R

object SuperToast {

    enum class Type { SUCCESS, ERROR, WARNING }

    fun show(context: Context, type: Type, title: String, message: String) {
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.custom_super_toast, null)

        val icon = layout.findViewById<ImageView>(R.id.toastIcon)
        val titleTv = layout.findViewById<TextView>(R.id.toastTitle)
        val msgTv = layout.findViewById<TextView>(R.id.toastMessage)
        val root = layout.findViewById<LinearLayout>(R.id.toastRoot)

        titleTv.text = title
        msgTv.text = message

        when (type) {
            Type.SUCCESS -> {
                root.setBackgroundResource(R.drawable.bg_super_toast)
                icon.setImageResource(R.drawable.ic_success_tick)
            }
            Type.ERROR -> {
                root.setBackgroundResource(R.drawable.bg_super_toast_error)
                icon.setImageResource(R.drawable.ic_error_cross)
            }
            Type.WARNING -> {
                root.setBackgroundResource(R.drawable.bg_super_toast_warning)
                icon.setImageResource(R.drawable.ic_warning)
            }
        }

        val toast = Toast(context)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 120)

        // ⭐ ENTRY ANIMATION
        layout.scaleX = 0.7f
        layout.scaleY = 0.7f
        layout.alpha = 0f
        layout.animate()
            .scaleX(1f)
            .scaleY(1f)
            .alpha(1f)
            .setDuration(320)
            .start()

        toast.show()
    }
}
