package com.example.bookmyhealth.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.bookmyhealth.R
import com.example.bookmyhealth.ui.splash.SplashActivity

object NotificationHelper {

    private const val CHANNEL_ID = "bookmyhealth_status_channel"
    private const val CHANNEL_NAME = "Health Updates"

    fun showRandomNotification(context: Context, message: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 1. Android 8.0+ ke liye Channel setup
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Daily health tips and appointment updates"
                setShowBadge(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        // 2. Custom Layouts set karna (RemoteViews)
        // Layout inflate karne ke liye R.layout use karo
        val collapsedView = RemoteViews(context.packageName, R.layout.notification_small)
        val expandedView = RemoteViews(context.packageName, R.layout.notification_large)

        // Bada view mein random message set karna
        expandedView.setTextViewText(R.id.notif_content_large, message)
        // Chota view mein brief text
        collapsedView.setTextViewText(R.id.notif_content_small, "Check your daily health tip! ✨")

        // 3. Click Action: Notification pe click karne se SplashActivity khulegi
        val intent = Intent(context, SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 4. Notification Builder
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Status bar icon (White/Transparent hona chahiye)
            .setCustomContentView(collapsedView)
            .setCustomBigContentView(expandedView)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle()) // Custom UI ke liye zaroori hai
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setAutoCancel(true) // Click karte hi gayab ho jaye
            .setContentIntent(pendingIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        // 5. Notification Show karna (Unique ID ke saath taaki purani wali replace na ho)
        val notificationId = (System.currentTimeMillis() % 10000).toInt()
        notificationManager.notify(notificationId, builder.build())
    }
}