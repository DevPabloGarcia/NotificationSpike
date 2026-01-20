# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Build the project
./gradlew build

# Clean build
./gradlew clean build

# Install debug APK on connected device
./gradlew installDebug

# Run unit tests
./gradlew test

# Run a single test class
./gradlew test --tests "com.pablo.dev.notificationspike.ExampleUnitTest"

# Run instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest
```

## Architecture

This is an Android Jetpack Compose application for testing Android notification functionality.

### Package Structure

- `notifications/data/` - Data layer with notification models and helper
  - `NotificationModels.kt` - Enums for `NotificationImportance` (URGENT, HIGH, MEDIUM, LOW) and `NotificationCategory` (ALARM, REMINDER, MESSAGE, etc.)
  - `NotificationHelper.kt` - Creates notification channels and manages showing/updating/canceling notifications

- `notifications/ui/` - UI layer with ViewModel and Compose screen
  - `NotificationsViewModel.kt` - State management using `StateFlow`, includes `Factory` for ViewModel creation without Hilt
  - `NotificationsScreen.kt` - Full Compose UI for testing notifications with permission handling

### Key Patterns

- **No Hilt/DI**: Uses manual ViewModel Factory pattern in `NotificationsViewModel.Factory`
- **State Management**: Uses `StateFlow` with `NotificationsUiState` data class
- **Notification Channels**: Four channels created based on importance levels (channel_urgent, channel_high, channel_medium, channel_low)
- **Permission Handling**: Handles `POST_NOTIFICATIONS` permission for Android 13+ (TIRAMISU)
