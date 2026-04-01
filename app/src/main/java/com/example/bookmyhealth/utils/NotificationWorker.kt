package com.example.bookmyhealth.utils

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // 1. Mast random messages ki list (Jitne chaho add kar lo)
            val healthTips = listOf(
                "Bhai, 8 glass paani peeya aaj? 💧 Health hi wealth hai!",
                "Doctor kehte hain, roz 15 min walk zaroori hai! 🏃‍♂️ Chalo utho!",
                "Apni reports check kar lijiye, sab up-to-date hai? 🏥",
                "BookMyHealth se aaj hi apna checkup schedule karein! ✨",
                "Stress kam lo, lambi saans lo. Stay Healthy! 🧘‍♂️",
                "Fruits khaye? Ek apple roz, doctor se door rakhega! 🍎",
                "Neend puri karo bhai, 7-8 ghante ki neend zaroori hai! 😴",
                "Aankhon ko rest do, screen se thoda door hat jao! 👁️"
            )

            // 2. Randomly ek message select karo
            val randomMessage = healthTips.random()

            // 3. NotificationHelper ko call karke notification dikhao
            NotificationHelper.showRandomNotification(applicationContext, randomMessage)

            // Success return karo taaki WorkManager ko pata chale kaam ho gaya
            Result.success()
        } catch (e: Exception) {
            // Agar kuch phata toh retry karo
            Result.retry()
        }
    }
}