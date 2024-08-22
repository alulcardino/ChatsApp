package com.vidial.chatsapp.presentation.ui.screens.sms

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.vidial.chatsapp.presentation.ui.components.navigation.ScreenRoute

@Composable
fun SmsCodeScreen(
    viewModel: SmsCodeViewModel = hiltViewModel(),
    navController: NavHostController,
    paddingValues: PaddingValues,
    phoneNumber: String
) {
    val state by viewModel.state.collectAsState()
    val effectFlow = viewModel.effect
    var code by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(effectFlow) {
        effectFlow.collect { effect ->
            when (effect) {
                is SmsCodeEffect.NavigateToChatList -> {
                    navController.navigate(ScreenRoute.ChatListScreen.route) {
                        popUpTo(ScreenRoute.SmsCodeScreen.route) { inclusive = true }
                    }
                }
                is SmsCodeEffect.NavigateToRegistration -> {
                    navController.navigate(ScreenRoute.RegistrationScreen.createRoute(effect.phoneNumber)) {
                        popUpTo(ScreenRoute.SmsCodeScreen.route) { inclusive = true }
                    }
                }
                is SmsCodeEffect.ShowErrorToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (state) {
            is SmsCodeState.Initial -> {
                TextField(
                    value = code,
                    onValueChange = { code = it },
                    label = { Text("Enter SMS Code") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        viewModel.handleIntent(SmsCodeIntent.VerifyCode(phoneNumber, code))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Verify Code")
                }
            }

            is SmsCodeState.Loading -> {
                CircularProgressIndicator()
            }

            is SmsCodeState.Error -> {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Error: ${(state as SmsCodeState.Error).message}",
                    color = Color.Red
                )
            }

            else -> Unit
        }
    }
}
