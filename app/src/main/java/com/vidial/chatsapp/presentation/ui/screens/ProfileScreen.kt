package com.vidial.chatsapp.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.vidial.chatsapp.data.remote.dto.UpdateProfileRequest
import com.vidial.chatsapp.data.remote.dto.UserProfile
import com.vidial.chatsapp.presentation.ui.components.navigation.ScreenRoute

@Composable
fun UserProfileContent(
    userProfile: UserProfile,
    onUpdateProfile: (UpdateProfileRequest) -> Unit,
    onLogout: () -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }

    var editableName by remember { mutableStateOf(userProfile.nickname) }
    var editableCity by remember { mutableStateOf(userProfile.city) }
    var editableBirthday by remember { mutableStateOf(userProfile.birthDate) }
    var editableAbout by remember { mutableStateOf(userProfile.about) }
    val zodiacSign = userProfile.zodiacSign

    val isBirthdayValid = editableBirthday.isNotBlank()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                // Аватарка пользователя
                AsyncImage(
                    model = userProfile.avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(MaterialTheme.shapes.medium)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Никнейм (неизменяемый)
                Text("Nickname: ${userProfile.nickname}")

                // Номер телефона (неизменяемый)
                Text("Phone: ${userProfile.phoneNumber}")

                // Город
                if (isEditing) {
                    EditableTextField(
                        label = "City",
                        value = editableCity,
                        onValueChange = { editableCity = it }
                    )
                } else {
                    Text("City: ${userProfile.city}")
                }

                // Дата рождения (обязательное поле)
                if (isEditing) {
                    EditableTextField(
                        label = "Birth Date",
                        value = editableBirthday,
                        onValueChange = { editableBirthday = it },
                        isRequired = true
                    )
                    if (!isBirthdayValid) {
                        Text(
                            text = "Birth Date is required",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }
                } else {
                    Text("Birth Date: ${userProfile.birthDate}")
                }

                // О себе
                if (isEditing) {
                    EditableTextField(
                        label = "About",
                        value = editableAbout,
                        onValueChange = { editableAbout = it }
                    )
                } else {
                    Text("About: ${userProfile.about}")
                }

                // Знак зодиака (только отображение)
                Text(
                    text = "Zodiac Sign: $zodiacSign",
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // Кнопка "Сохранить" при редактировании
                if (isEditing) {
                    Button(
                        onClick = {
                            if (isBirthdayValid) {
                                onUpdateProfile(
                                    UpdateProfileRequest(
                                        name = editableName,
                                        username = userProfile.nickname,
                                        birthday = editableBirthday,
                                        city = editableCity,
                                        vk = null,
                                        instagram = null,
                                        status = editableAbout,
                                    )
                                )
                                isEditing = false
                            }
                        },
                        enabled = isBirthdayValid,
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("Save")
                    }
                }

                // Добавляем кнопку выхода
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Logout")
                }
            }
        }

        // Кнопка редактирования
        Button(
            onClick = {
                if (isEditing) {
                    editableName = userProfile.nickname
                    editableCity = userProfile.city
                    editableBirthday = userProfile.birthDate
                    editableAbout = userProfile.about
                }
                isEditing = !isEditing
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text(if (isEditing) "Cancel" else "Edit")
        }
    }
}

@Composable
fun EditableTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isRequired: Boolean = false
) {
    Column {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Row {
                    Text(label)
                    if (isRequired) {
                        Text(
                            text = " *",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 16.sp
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ProfileScreen(
    viewModel: UserProfileViewModel = hiltViewModel(),
    navController: NavHostController // Добавляем navController для управления навигацией
) {
    val state by viewModel.userProfileState.collectAsState()

    when (state) {
        is UserProfileState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is UserProfileState.Success -> {
            val userProfile = (state as UserProfileState.Success).userProfile
            UserProfileContent(
                userProfile = userProfile,
                onUpdateProfile = { profileRequest ->
                    viewModel.updateProfile(profileRequest)
                },
                onLogout = {
                    viewModel.logout()
                    navController.navigate(ScreenRoute.PhoneNumberScreen.route) {
                        popUpTo(0) // Удаляем все из backstack, чтобы пользователь не мог вернуться назад
                    }
                }
            )
        }
        is UserProfileState.Error -> {
            val message = (state as UserProfileState.Error).message
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = message, color = MaterialTheme.colorScheme.error, fontSize = 18.sp)
            }
        }
    }
}


