package com.pablo.dev.notificationspike.notifications.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pablo.dev.notificationspike.notifications.data.NotificationCategory
import com.pablo.dev.notificationspike.notifications.data.NotificationHelper
import com.pablo.dev.notificationspike.notifications.data.NotificationImportance
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class NotificationsUiState(
    val notificationsEnabled: Boolean = true,
    val selectedImportance: NotificationImportance = NotificationImportance.URGENT,
    val selectedCategory: NotificationCategory? = null,
    val customTitle: String = "Test Notification",
    val customMessage: String = "This is a test notification message",
    val isProgressRunning: Boolean = false,
    val progressNotificationId: Int? = null
)

class NotificationsViewModel(
    private val notificationHelper: NotificationHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    init {
        checkNotificationStatus()
    }

    fun checkNotificationStatus() {
        _uiState.value = _uiState.value.copy(
            notificationsEnabled = notificationHelper.areNotificationsEnabled()
        )
    }

    fun onImportanceSelected(importance: NotificationImportance) {
        _uiState.value = _uiState.value.copy(selectedImportance = importance)
    }

    fun onCategorySelected(category: NotificationCategory?) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun onTitleChanged(title: String) {
        _uiState.value = _uiState.value.copy(customTitle = title)
    }

    fun onMessageChanged(message: String) {
        _uiState.value = _uiState.value.copy(customMessage = message)
    }

    fun sendTestNotification() {
        val state = _uiState.value
        notificationHelper.showNotification(
            title = state.customTitle,
            message = state.customMessage,
            importance = state.selectedImportance,
            category = state.selectedCategory
        )
    }

    fun sendNotificationWithImportance(importance: NotificationImportance) {
        notificationHelper.showNotification(
            title = "${importance.displayName} Notification",
            message = importance.description,
            importance = importance
        )
    }

    fun sendNotificationWithCategory(category: NotificationCategory) {
        notificationHelper.showNotification(
            title = "${category.displayName} Notification",
            message = category.description,
            importance = NotificationImportance.URGENT,
            category = category
        )
    }

    fun sendProgressNotification() {
        if (_uiState.value.isProgressRunning) return

        viewModelScope.launch {
            val notificationId = notificationHelper.showNotificationWithProgress(
                title = "Downloading...",
                message = "0%",
                progress = 0
            )

            _uiState.value = _uiState.value.copy(
                isProgressRunning = true,
                progressNotificationId = notificationId
            )

            for (progress in 1..100 step 5) {
                delay(200)
                notificationHelper.updateNotificationProgress(
                    notificationId = notificationId,
                    title = "Downloading...",
                    message = "$progress%",
                    progress = progress
                )
            }

            notificationHelper.updateNotificationProgress(
                notificationId = notificationId,
                title = "Download Complete",
                message = "File downloaded successfully",
                progress = 100
            )

            _uiState.value = _uiState.value.copy(
                isProgressRunning = false,
                progressNotificationId = null
            )
        }
    }

    fun cancelAllNotifications() {
        notificationHelper.cancelAllNotifications()
        _uiState.value = _uiState.value.copy(
            isProgressRunning = false,
            progressNotificationId = null
        )
    }

    class Factory(private val notificationHelper: NotificationHelper) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NotificationsViewModel::class.java)) {
                return NotificationsViewModel(notificationHelper) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
