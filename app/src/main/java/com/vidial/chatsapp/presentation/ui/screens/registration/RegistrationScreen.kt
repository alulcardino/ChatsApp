package com.vidial.chatsapp.presentation.ui.screens.registration

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
fun RegistrationScreen(
    viewModel: RegistrationViewModel = hiltViewModel(),
    navController: NavHostController,
    paddingValues: PaddingValues,
    phoneNumber: String
) {
    val state by viewModel.state.collectAsState()
    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(32.dp)) {
        Text("Phone Number: $phoneNumber")

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii)
        )
        Button(
            onClick = {
                viewModel.registerUser(phoneNumber, name, username)
            }
        ) {
            Text("Register")
        }

        when (val currentState = state) {
            is RegistrationState.Loading -> {
                CircularProgressIndicator()
            }
            is RegistrationState.Success -> {
                navController.navigate(ScreenRoute.ChatListScreen.route)
            }
            is RegistrationState.Error -> {
                Text("Error: ${currentState.message}")
            }
            RegistrationState.Initial -> {
            }
        }
    }
}
