package com.vidial.chatsapp.presentation.ui.screens

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.vidial.chatsapp.presentation.ui.components.navigation.ScreenRoute

@Composable
fun SmsCodeScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navController: NavHostController,
    phoneNumber: String,
    paddingValues: PaddingValues
) {
    var smsCode by remember { mutableStateOf("") }
    val context = LocalContext.current

    val uiState = viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Input for SMS code
        TextField(
            value = smsCode,
            onValueChange = { newValue ->
                if (newValue.length <= 6 && newValue.all { it.isDigit() }) {
                    smsCode = newValue
                }
            },
            placeholder = { Text("Enter SMS code") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button to verify code
        Button(onClick = {
            if (smsCode.length == 6) {
                viewModel.verifyCode(phoneNumber, smsCode)
            } else {
                Toast.makeText(context, "Please enter a valid 6-digit code", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text(text = "Verify Code")
        }

        // Handle UI State
        when (uiState.value) {
            is AuthUiState.Loading -> {
                CircularProgressIndicator()
            }
            is AuthUiState.Error -> {
                Toast.makeText(context, (uiState.value as AuthUiState.Error).message, Toast.LENGTH_SHORT).show()
            }
            is AuthUiState.Authorized -> {
                navController.navigate(ScreenRoute.ChatListScreen.route) {
                    popUpTo(ScreenRoute.PhoneNumberScreen.route) {
                        inclusive = true
                    }
                }
            }
            is AuthUiState.NeedsRegistration -> {
                navController.navigate(ScreenRoute.RegistrationScreen.route)
            }
            else -> {}
        }
    }
}
