package com.vidial.chatsapp.presentation.ui.screens.phone

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.vidial.chatsapp.presentation.ui.components.navigation.ScreenRoute

@Composable
fun PhoneNumberScreen(
    viewModel: PhoneNumberViewModel = hiltViewModel(),
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    val state by viewModel.state.collectAsState()
    var phoneNumber by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(32.dp)) {
        when (state) {
            is PhoneNumberState.Initial -> {
                TextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
                Button(
                    onClick = {
                        viewModel.sendPhoneNumber(phoneNumber)
                        navController.navigate(ScreenRoute.SmsCodeScreen.createRoute(phoneNumber))
                    }
                ) {
                    Text("Send Code")
                }
            }

            is PhoneNumberState.Loading -> {
                CircularProgressIndicator()
            }

            is PhoneNumberState.Success -> {
                // Navigation already handled in the onClick
            }

            is PhoneNumberState.Error -> {
                Text("Error: ${(state as PhoneNumberState.Error).message}")
            }
        }
    }
}
