package com.vidial.chatsapp.presentation.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.vidial.chatsapp.presentation.ui.components.navigation.MainNavHost
import com.vidial.chatsapp.presentation.ui.components.scafford.Root

@Composable
fun App() {
    val navController = rememberNavController()

    Root(navController = navController) { paddingValues ->
        MainNavHost(
            navController = navController,
            paddingValues = paddingValues
        )
    }
}
