package com.pablo.dev.notificationspike.notifications.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.pablo.dev.notificationspike.MainActivity
import com.pablo.dev.notificationspike.R

class NotificationHelper(private val context: Context) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private var notificationId = 1

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()

        val channels = NotificationImportance.entries.map { importance ->
            NotificationChannel(
                importance.channelId,
                importance.displayName,
                importance.importance
            ).apply {
                description = importance.description
                if (importance == NotificationImportance.URGENT || importance == NotificationImportance.HIGH) {
                    setSound(soundUri, audioAttributes)
                    enableVibration(true)
                    vibrationPattern = longArrayOf(0, 250, 250, 250)
                }
                if (importance == NotificationImportance.URGENT) {
                    enableLights(true)
                    lightColor = android.graphics.Color.RED
                }
            }
        }

        notificationManager.createNotificationChannels(channels)
    }

    fun showNotification(
        title: String,
        message: String,
        importance: NotificationImportance,
        category: NotificationCategory? = null
    ): Int {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, importance.channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))

        // Set priority based on importance (for pre-Oreo devices)
        when (importance) {
            NotificationImportance.URGENT -> {
                builder.priority = NotificationCompat.PRIORITY_HIGH
                builder.setDefaults(NotificationCompat.DEFAULT_ALL)
            }
            NotificationImportance.HIGH -> {
                builder.priority = NotificationCompat.PRIORITY_DEFAULT
                builder.setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE)
            }
            NotificationImportance.MEDIUM -> {
                builder.priority = NotificationCompat.PRIORITY_LOW
            }
            NotificationImportance.LOW -> {
                builder.priority = NotificationCompat.PRIORITY_MIN
            }
        }

        category?.let {
            builder.setCategory(it.category)
        }

        val currentId = notificationId++
        notificationManager.notify(currentId, builder.build())
        return currentId
    }

    fun showNotificationWithProgress(
        title: String,
        message: String,
        progress: Int,
        maxProgress: Int = 100
    ): Int {
        val builder = NotificationCompat.Builder(context, NotificationImportance.MEDIUM.channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .setProgress(maxProgress, progress, false)
            .setOngoing(progress < maxProgress)
            .setAutoCancel(progress >= maxProgress)

        val currentId = notificationId++
        notificationManager.notify(currentId, builder.build())
        return currentId
    }

    fun updateNotificationProgress(
        notificationId: Int,
        title: String,
        message: String,
        progress: Int,
        maxProgress: Int = 100
    ) {
        val builder = NotificationCompat.Builder(context, NotificationImportance.MEDIUM.channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .setProgress(maxProgress, progress, false)
            .setOngoing(progress < maxProgress)
            .setAutoCancel(progress >= maxProgress)

        notificationManager.notify(notificationId, builder.build())
    }

    fun cancelNotification(notificationId: Int) {
        notificationManager.cancel(notificationId)
    }

    fun cancelAllNotifications() {
        notificationManager.cancelAll()
    }

    fun areNotificationsEnabled(): Boolean {
        return notificationManager.areNotificationsEnabled()
    }

    fun getChannelStatus(channelId: String): String {
        val channel = notificationManager.getNotificationChannel(channelId)
        return when (channel?.importance) {
            NotificationManager.IMPORTANCE_NONE -> "Blocked"
            NotificationManager.IMPORTANCE_MIN -> "Low (Silent)"
            NotificationManager.IMPORTANCE_LOW -> "Medium (Silent)"
            NotificationManager.IMPORTANCE_DEFAULT -> "High (Sound)"
            NotificationManager.IMPORTANCE_HIGH -> "Urgent (Sound + Heads-up)"
            else -> "Unknown"
        }
    }
}
