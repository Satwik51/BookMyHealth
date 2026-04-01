package com.example.bookmyhealth.utils

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import java.util.EnumMap

object QRUtils {

    fun generateQRCode(rawText: String): Bitmap? {
        val size = 512

        return try {

            // 🔥 STEP 1: CLEAN INPUT (VERY IMPORTANT)
            val text = rawText
                .trim()
                .replace("\n", "")
                .replace("\r", "")
                .replace(" ", "")
                // allow only Firebase safe characters
                .replace(Regex("[^a-zA-Z0-9_-]"), "")

            if (text.isEmpty()) return null

            // 🔥 STEP 2: ZXING CONFIG
            val hints: MutableMap<EncodeHintType, Any> = EnumMap(EncodeHintType::class.java)
            hints[EncodeHintType.MARGIN] = 1
            hints[EncodeHintType.CHARACTER_SET] = "UTF-8"

            val bitMatrix = MultiFormatWriter().encode(
                text,
                BarcodeFormat.QR_CODE,
                size,
                size,
                hints
            )

            // 🔥 STEP 3: BITMAP CREATE (OPTIMIZED)
            val width = bitMatrix.width
            val height = bitMatrix.height

            val pixels = IntArray(width * height)

            for (y in 0 until height) {
                val offset = y * width
                for (x in 0 until width) {
                    pixels[offset + x] =
                        if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE
                }
            }

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)

            bitmap

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}