# Android Test Task

## Overview

This project is a sample Android application showcasing a user profile screen with features like avatar selection, editable profile details and list of chats. It is designed to demonstrate the ability to build a clean and functional UI using Jetpack Compose and manage state with ViewModel and Hilt for dependency injection.

## Features

- **User Profile Screen**: Allows users to view and edit their profile information, including name, city, birthday, and status.
- **Avatar Selection**: Users can select and update their avatar from the gallery.
- **Navigation**: Uses Jetpack Compose Navigation for screen transitions.

## Getting Started

### Prerequisites

Ensure you have the following tools installed:
- Android Studio (Arctic Fox or later)
- JDK 11 or later
- Emulator or a physical device for testing

### Installation

1. **Clone the Repository**
   ```bash
   git clone https://github.com/alulcardino/ChatsApp.git
   cd ChatsApp
   ```

2. **Open the Project**
   Open Android Studio and select "Open an existing project", then navigate to the cloned repository and open it.

3. **Build the Project**
   Sync the project with Gradle files by clicking "Sync Now" in the Android Studio toolbar.

4. **Run the Application**
   You can now run the application on an emulator or a physical device by selecting the appropriate target and clicking the "Run" button in Android Studio.

## Configuration

- **Dependency Injection**: Uses Hilt for dependency injection. Ensure you have `@AndroidEntryPoint` annotations on activities and fragments.
- **State Management**: ViewModels are used to manage UI-related data in a lifecycle-conscious way.
- **Navigation**: Jetpack Compose Navigation is used for handling in-app navigation.


## Known Issues

- **Avatar Selection**: Ensure the app has permissions to access external storage on devices running Android 10 or lower.
- **Date Validation**: The date input format is dd.mm.yyyy. The app may not handle all edge cases for date validation.

## Contributing

Feel free to fork the repository and submit pull requests. Ensure that your code follows the project's coding style and includes appropriate tests.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
