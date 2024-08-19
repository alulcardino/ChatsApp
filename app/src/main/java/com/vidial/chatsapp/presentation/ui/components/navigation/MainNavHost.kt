package com.vidial.chatsapp.presentation.ui.components.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.vidial.chatsapp.presentation.ui.screens.ChatListScreen
import com.vidial.chatsapp.presentation.ui.screens.ChatScreen
import com.vidial.chatsapp.presentation.ui.screens.EditProfileScreen
import com.vidial.chatsapp.presentation.ui.screens.PhoneNumberScreen
import com.vidial.chatsapp.presentation.ui.screens.ProfileScreen
import com.vidial.chatsapp.presentation.ui.screens.RegistrationScreen
import com.vidial.chatsapp.presentation.ui.screens.SmsCodeScreen

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
                PhoneNumberScreen(navController, paddingValues)
            }
            composable(ScreenRoute.SmsCodeScreen.route) {
                SmsCodeScreen(navController, paddingValues)
            }
        }

        // Регистрация
        composable(ScreenRoute.RegistrationScreen.route) {
            RegistrationScreen(navController, paddingValues)
        }

        // Чаты
        navigation(
            startDestination = ScreenRoute.ChatListScreen.route,
            route = ScreenRoute.ChatsGraph.route
        ) {
            composable(ScreenRoute.ChatListScreen.route) {
                ChatListScreen(navController, paddingValues)
            }
            composable(ScreenRoute.ChatScreen.route) {
                ChatScreen(navController, paddingValues)
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
