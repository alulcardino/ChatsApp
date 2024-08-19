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
fun PhoneNumberScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    var phoneNumber by remember { mutableStateOf("") }
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
        // Input for phone number
        TextField(
            value = phoneNumber,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                    phoneNumber = newValue
                }
            },
            placeholder = { Text("Enter your phone number") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button to send phone number
        Button(onClick = {
            if (phoneNumber.isNotEmpty()) {
                viewModel.sendPhoneNumber(phoneNumber)
            } else {
                Toast.makeText(context, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text(text = "Send Code")
        }

        when (uiState.value) {
            is AuthUiState.Loading -> {
                CircularProgressIndicator()
            }
            is AuthUiState.Error -> {
                Toast.makeText(context, (uiState.value as AuthUiState.Error).message, Toast.LENGTH_SHORT).show()
            }
            is AuthUiState.CodeSent -> {
                navController.navigate(ScreenRoute.SmsCodeScreen.route)
            }
            else -> {}
        }
    }
}
