package com.vidial.chatsapp.presentation.ui.screens.sms

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.vidial.chatsapp.R
import com.vidial.chatsapp.presentation.ui.components.ChatButton
import com.vidial.chatsapp.presentation.ui.components.ChatsAppBar
import com.vidial.chatsapp.presentation.ui.components.ErrorScreen
import com.vidial.chatsapp.presentation.ui.components.LoadingScreen
import com.vidial.chatsapp.presentation.ui.components.navigation.ScreenRoute
import com.vidial.chatsapp.presentation.ui.theme.DarkPurple
import com.vidial.chatsapp.presentation.ui.theme.LightGray
import com.vidial.chatsapp.presentation.ui.theme.LightPurple


@Composable
fun SmsCodeScreen(
    viewModel: SmsCodeViewModel = hiltViewModel(),
    navController: NavHostController,
    paddingValues: PaddingValues,
    phoneNumber: String
) {
    val state by viewModel.state.collectAsState()
    val otpValues by viewModel.otpValues.collectAsState()
    val effectFlow = viewModel.effect
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

    ChatsAppBar(
        title = stringResource(R.string.sms),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (state) {
            is SmsCodeState.Initial -> {
                OtpInputSection(
                    otpValues = otpValues,
                    onOtpValueChange = { index, newValue -> viewModel.onOtpValueChange(index, newValue) },
                    onFocusChange = { index, isNext -> viewModel.onFocusChange(index, isNext) },
                    onVerifyCodeClick = { viewModel.verifyCode(phoneNumber) }
                )
            }

            is SmsCodeState.Loading -> {
                LoadingScreen(innerPadding = paddingValues)
            }

            is SmsCodeState.Error -> {
                ErrorScreen(message = (state as SmsCodeState.Error).message, innerPadding = paddingValues)
            }

            else -> Unit
        }
    }
}

@Composable
fun OtpInputSection(
    otpValues: List<String>,
    onOtpValueChange: (index: Int, newValue: String) -> Unit,
    onFocusChange: (index: Int, isNext: Boolean) -> Unit,
    onVerifyCodeClick: () -> Unit
) {
    val focusRequesters = remember { List(otpValues.size) { FocusRequester() } }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            otpValues.forEachIndexed { index, value ->
                TextField(
                    value = value,
                    onValueChange = { newValue ->
                        onOtpValueChange(index, newValue)
                        if (newValue.isNotEmpty() && index < otpValues.size - 1) {
                            focusRequesters[index + 1].requestFocus()
                        } else if (newValue.isEmpty() && index > 0) {
                            focusRequesters[index - 1].requestFocus()
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .width(42.dp)
                        .height(60.dp)
                        .focusRequester(focusRequesters[index]),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = LightPurple,
                        unfocusedContainerColor = LightPurple,
                        disabledContainerColor = LightGray,
                        focusedIndicatorColor = DarkPurple,
                        unfocusedIndicatorColor = DarkPurple,
                        disabledTextColor = Color.Black,
                        cursorColor = DarkPurple,
                        disabledIndicatorColor = Color.Transparent,
                        focusedSupportingTextColor = DarkPurple,
                        disabledSupportingTextColor = LightPurple,
                        unfocusedLabelColor = DarkPurple,
                        focusedLabelColor = DarkPurple,
                        disabledLabelColor = DarkPurple,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    textStyle = TextStyle(color = Color.Black)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ChatButton(text = stringResource(R.string.verify_code), onClick = onVerifyCodeClick)
    }
}
