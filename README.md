# NotificationSpike

Android application for testing and exploring Android notification functionality with different importance levels, categories, and features.

## Features

- **Importance Levels**: Test notifications with different priorities (Urgent, High, Medium, Low)
- **Notification Categories**: Test different notification categories (Alarm, Reminder, Message, Call, Event, Progress, Social, Error, Status)
- **Custom Notifications**: Create notifications with custom title and message
- **Progress Notifications**: Demo of progress-style notifications with updates
- **Permission Handling**: Proper handling of POST_NOTIFICATIONS permission for Android 13+

## Requirements

- Android Studio
- Min SDK: 28 (Android 9.0)
- Target SDK: 36

## Build & Run

```bash
# Build the project
./gradlew build

# Install on connected device
./gradlew installDebug
```

## Project Structure

```
app/src/main/java/com/pablo/dev/notificationspike/
├── MainActivity.kt
├── notifications/
│   ├── data/
│   │   ├── NotificationHelper.kt
│   │   └── NotificationModels.kt
│   └── ui/
│       ├── NotificationsScreen.kt
│       └── NotificationsViewModel.kt
└── ui/theme/
    ├── Color.kt
    ├── Theme.kt
    └── Type.kt
```

## Tech Stack

- Kotlin
- Jetpack Compose
- Material 3
- ViewModel with StateFlow
