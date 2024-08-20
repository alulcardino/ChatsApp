package com.vidial.chatsapp.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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

@Composable
fun UserProfileContent(
    userProfile: UserProfile,
    onUpdateProfile: (UpdateProfileRequest) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }  // Состояние режима редактирования

    // Эти переменные будут использоваться для редактирования, если isEditing = true
    var editableName by remember { mutableStateOf(userProfile.nickname) }
    var editablePhone by remember { mutableStateOf(userProfile.phoneNumber) }
    var editableCity by remember { mutableStateOf(userProfile.city) }
    var editableBirthday by remember { mutableStateOf(userProfile.birthDate) }
    var editableAbout by remember { mutableStateOf(userProfile.about) }
    val zodiacSign = userProfile.zodiacSign

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

                // Никнейм
                if (isEditing) {
                    EditableTextField(
                        label = "Nickname",
                        value = editableName,
                        onValueChange = { editableName = it }
                    )
                } else {
                    Text("Nickname: ${userProfile.nickname}")
                }

                // Номер телефона
                if (isEditing) {
                    EditableTextField(
                        label = "Phone",
                        value = editablePhone,
                        onValueChange = { editablePhone = it }
                    )
                } else {
                    Text("Phone: ${userProfile.phoneNumber}")
                }

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

                // Дата рождения
                if (isEditing) {
                    EditableTextField(
                        label = "Birth Date",
                        value = editableBirthday,
                        onValueChange = { editableBirthday = it }
                    )
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

                // Если в режиме редактирования, добавляем кнопку "Сохранить"
                if (isEditing) {
                    Button(
                        onClick = {
                            // Отправляем новые данные на сервер
                            onUpdateProfile(
                                UpdateProfileRequest(
                                    name = editableName,
                                    username = editableName,
                                    birthday = editableBirthday,
                                    city = editableCity,
                                    vk = null,               // Значение VK, если есть
                                    instagram = null,        // Значение Instagram, если есть
                                    status = editableAbout,
                                )
                            )
                            isEditing = false // Выход из режима редактирования после сохранения
                        },
                    ) {
                        Text("Save")
                    }
                }
            }
        }

        // Кнопка редактирования в правом верхнем углу
        Button(
            onClick = {
                if (isEditing) {
                    // Если отмена, возвращаем все значения, которые были изначально
                    editableName = userProfile.nickname
                    editablePhone = userProfile.phoneNumber
                    editableCity = userProfile.city
                    editableBirthday = userProfile.birthDate
                    editableAbout = userProfile.about
                }
                isEditing = !isEditing  // Переключаем режим редактирования
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
    onValueChange: (String) -> Unit
) {
    Column {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ProfileScreen(
    viewModel: UserProfileViewModel = hiltViewModel()
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
            UserProfileContent(userProfile = userProfile) { profileRequest ->
                viewModel.updateProfile(profileRequest)
            }
        }
        is UserProfileState.Error -> {
            val message = (state as UserProfileState.Error).message
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = message, color = MaterialTheme.colorScheme.error, fontSize = 18.sp)
            }
        }
    }
}
