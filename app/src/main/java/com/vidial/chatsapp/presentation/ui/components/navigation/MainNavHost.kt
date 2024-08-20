package com.vidial.chatsapp.presentation.ui.components.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.vidial.chatsapp.presentation.ui.screens.chatList.ChatListScreen
import com.vidial.chatsapp.presentation.ui.screens.chat.ChatScreen
import com.vidial.chatsapp.presentation.ui.screens.EditProfileScreen
import com.vidial.chatsapp.presentation.ui.screens.phone.PhoneNumberScreen
import com.vidial.chatsapp.presentation.ui.screens.ProfileScreen
import com.vidial.chatsapp.presentation.ui.screens.registration.RegistrationScreen
import com.vidial.chatsapp.presentation.ui.screens.sms.SmsCodeScreen

@Composable
fun MainNavHost(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = ScreenRoute.LoginGraph.route
    ) {
        // Авторизация
        navigation(
            startDestination = ScreenRoute.PhoneNumberScreen.route,
            route = ScreenRoute.LoginGraph.route
        ) {
            composable(ScreenRoute.PhoneNumberScreen.route) {
                PhoneNumberScreen(navController = navController, paddingValues = paddingValues)
            }
            composable(ScreenRoute.SmsCodeScreen.route) { backStackEntry ->
                SmsCodeScreen(
                    navController = navController,
                    paddingValues = paddingValues,
                    phoneNumber = backStackEntry.arguments?.getString("phone") ?: ""
                )
            }
            composable(ScreenRoute.RegistrationScreen.route) { backStackEntry ->
                RegistrationScreen(
                    navController = navController,
                    paddingValues = paddingValues,
                    phoneNumber = backStackEntry.arguments?.getString("phone") ?: ""
                )
            }
        }



        // Чаты
        navigation(
            startDestination = ScreenRoute.ChatListScreen.route,
            route = ScreenRoute.ChatsGraph.route
        ) {
            composable(ScreenRoute.ChatListScreen.route) {
                ChatListScreen(navController, paddingValues)
            }
            composable(ScreenRoute.ChatScreen.route) { backStackEntry ->
                val chatId = backStackEntry.arguments?.getString("chatId")?.toIntOrNull() ?: 0
                ChatScreen(navController = navController, paddingValues = paddingValues, chatId = chatId)
            }
        }

        // Профиль
        navigation(
            startDestination = ScreenRoute.ProfileScreen.route,
            route = ScreenRoute.ProfileGraph.route
        ) {
            composable(ScreenRoute.ProfileScreen.route) {
                ProfileScreen(navController, paddingValues)
            }
            composable(ScreenRoute.EditProfileScreen.route) {
                EditProfileScreen(navController, paddingValues)
            }
        }
    }
}
