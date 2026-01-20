package com.pablo.dev.notificationspike

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pablo.dev.notificationspike.notifications.data.NotificationHelper
import com.pablo.dev.notificationspike.notifications.ui.NotificationsScreen
import com.pablo.dev.notificationspike.notifications.ui.NotificationsViewModel
import com.pablo.dev.notificationspike.ui.theme.NotificationSpikeTheme

class MainActivity : ComponentActivity() {

    private lateinit var notificationHelper: NotificationHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        notificationHelper = NotificationHelper(applicationContext)

        enableEdgeToEdge()
        setContent {
            NotificationSpikeTheme {
                val viewModel: NotificationsViewModel = viewModel(
                    factory = NotificationsViewModel.Factory(notificationHelper)
                )
                NotificationsScreen(viewModel = viewModel)
            }
        }
    }
}
