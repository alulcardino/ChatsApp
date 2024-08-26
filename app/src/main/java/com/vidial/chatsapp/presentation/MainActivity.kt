package com.vidial.chatsapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.compose.rememberNavController
import com.vidial.chatsapp.domain.provider.TokenProvider
import com.vidial.chatsapp.presentation.ui.App
import com.vidial.chatsapp.presentation.ui.components.navigation.MainNavHost
import com.vidial.chatsapp.presentation.ui.components.navigation.ScreenRoute
import com.vidial.chatsapp.presentation.ui.theme.ChatsAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var tokenProvider: TokenProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ChatsAppTheme {
                App(tokenProvider = tokenProvider)
            }
        }
    }
}
