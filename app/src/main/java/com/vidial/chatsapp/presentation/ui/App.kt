package com.vidial.chatsapp.presentation.ui

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.vidial.chatsapp.presentation.ui.components.navigation.MainNavHost

@Composable
fun App() {
    val navController = rememberNavController()

    Scaffold { paddingValues ->
        MainNavHost(
            navController = navController,
            paddingValues = paddingValues
        )
    }
}
