package com.vidial.chatsapp.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.vidial.chatsapp.domain.provider.TokenProvider
import com.vidial.chatsapp.presentation.ui.components.navigation.MainNavHost
import com.vidial.chatsapp.presentation.ui.components.navigation.ScreenRoute
import com.vidial.chatsapp.presentation.ui.components.scafford.Root

@Composable
fun App(
    tokenProvider: TokenProvider
) {
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        if (tokenProvider.isUserLoggedIn()) {
            navController.navigate(ScreenRoute.ChatsGraph.route) {
                popUpTo(ScreenRoute.LoginGraph.route) { inclusive = true }
            }
        } else {
            navController.navigate(ScreenRoute.LoginGraph.route) {
                popUpTo(ScreenRoute.ChatsGraph.route) { inclusive = true }
            }
        }
    }

    Root(navController = navController) { paddingValues ->
        MainNavHost(
            navController = navController,
            paddingValues = paddingValues,
            startDestination = ScreenRoute.LoginGraph.route
        )
    }
}
