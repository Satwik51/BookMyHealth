package com.example.bookmyhealth.utils

import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.journeyapps.barcodescanner.CaptureActivity

class CaptureActivityPortrait : CaptureActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val textView = TextView(this).apply {
            text = "📱 BookMyHealth Verifier\nScan Patient Booking QR"
            textSize = 18f
            setTextColor(android.graphics.Color.WHITE)
            setTypeface(null, android.graphics.Typeface.BOLD)
            gravity = Gravity.CENTER
            setPadding(20, 60, 20, 0) // 🔥 TOP SPACE
        }

        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.TOP // 🔥 FIX: TOP PE FIX KAR DIYA
            topMargin = 80        // 🔥 QR box se upar shift
        }

        val root = window.decorView as ViewGroup
        root.addView(textView, params)
    }
}