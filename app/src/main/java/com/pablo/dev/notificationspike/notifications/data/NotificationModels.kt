package com.pablo.dev.notificationspike.notifications.data

import android.app.NotificationManager
import androidx.core.app.NotificationCompat

enum class NotificationImportance(
    val displayName: String,
    val description: String,
    val channelId: String,
    val importance: Int
) {
    URGENT(
        displayName = "Urgent",
        description = "Makes sound and appears as heads-up notification",
        channelId = "channel_urgent",
        importance = NotificationManager.IMPORTANCE_HIGH
    ),
    HIGH(
        displayName = "High",
        description = "Makes sound",
        channelId = "channel_high",
        importance = NotificationManager.IMPORTANCE_DEFAULT
    ),
    MEDIUM(
        displayName = "Medium",
        description = "No sound",
        channelId = "channel_medium",
        importance = NotificationManager.IMPORTANCE_LOW
    ),
    LOW(
        displayName = "Low",
        description = "No sound and doesn't appear in status bar",
        channelId = "channel_low",
        importance = NotificationManager.IMPORTANCE_MIN
    )
}

enum class NotificationCategory(
    val displayName: String,
    val description: String,
    val category: String
) {
    ALARM(
        displayName = "Alarm",
        description = "Alarm or timer",
        category = NotificationCompat.CATEGORY_ALARM
    ),
    REMINDER(
        displayName = "Reminder",
        description = "User-scheduled reminder",
        category = NotificationCompat.CATEGORY_REMINDER
    ),
    MESSAGE(
        displayName = "Message",
        description = "Incoming message (SMS, chat)",
        category = NotificationCompat.CATEGORY_MESSAGE
    ),
    CALL(
        displayName = "Call",
        description = "Incoming call (voice/video)",
        category = NotificationCompat.CATEGORY_CALL
    ),
    EVENT(
        displayName = "Event",
        description = "Calendar event",
        category = NotificationCompat.CATEGORY_EVENT
    ),
    PROGRESS(
        displayName = "Progress",
        description = "Progress of a long-running operation",
        category = NotificationCompat.CATEGORY_PROGRESS
    ),
    SOCIAL(
        displayName = "Social",
        description = "Social network or sharing update",
        category = NotificationCompat.CATEGORY_SOCIAL
    ),
    ERROR(
        displayName = "Error",
        description = "Error in background operation",
        category = NotificationCompat.CATEGORY_ERROR
    ),
    STATUS(
        displayName = "Status",
        description = "Ongoing information about device status",
        category = NotificationCompat.CATEGORY_STATUS
    )
}
