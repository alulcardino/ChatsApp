package com.vidial.chatsapp.presentation.ui.screens

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.vidial.chatsapp.R
import com.vidial.chatsapp.domain.model.AvatarModel
import com.vidial.chatsapp.domain.model.UpdateProfileModel
import com.vidial.chatsapp.domain.model.UserProfileModel
import com.vidial.chatsapp.presentation.ui.components.CoilImage
import com.vidial.chatsapp.presentation.ui.components.navigation.ScreenRoute
import java.io.ByteArrayOutputStream
import java.util.Base64

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserProfileContent(
    userProfile: UserProfileModel,
    onUpdateProfile: (UpdateProfileModel) -> Unit,
    onLogout: () -> Unit
) {
    Log.d("UserProfileContent", "Rendering UserProfileContent with: $userProfile")

    val username = userProfile.username.ifBlank { "Unknown" }
    val city = userProfile.city.ifBlank { "Not provided" }
    val birthday = if (userProfile.birthDate.isBlank()) "Birthday not provided" else userProfile.birthDate
    val avatarUrl = userProfile.avatarUrl?.ifBlank { "" }

    var isEditing by remember { mutableStateOf(false) }
    var avatarUri by remember { mutableStateOf<Uri?>(null) }
    var editableName by remember { mutableStateOf(username) }
    var editableCity by remember { mutableStateOf(city) }
    var editableBirthday by remember { mutableStateOf(birthday) }
    val zodiacSign = userProfile.zodiacSign

    val isBirthdayValid = editableBirthday.isNotBlank()

    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val selectedImageUri: Uri? = data?.data
            if (selectedImageUri != null) {
                avatarUri = selectedImageUri
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                        .padding(4.dp)
                        .clickable(enabled = isEditing) {
                            val pickImageIntent = Intent(Intent.ACTION_PICK).apply {
                                type = "image/*"
                            }
                            imagePickerLauncher.launch(pickImageIntent)
                        }
                ) {
                    CoilImage(
                        imageUrl = avatarUri?.toString() ?: avatarUrl,
                        contentDescription = "User Avatar",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(MaterialTheme.shapes.medium),
                        defaultImageResId = R.drawable.chat_default
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("Nickname: $username")
                Text("Phone: ${userProfile.phone}")

                if (isEditing) {
                    EditableTextField(
                        label = "City",
                        value = editableCity,
                        onValueChange = { editableCity = it }
                    )
                } else {
                    Text("City: $city")
                }

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
                    Text("Birth Date: $birthday")
                }

                Text(
                    text = "Zodiac Sign: $zodiacSign",
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                if (isEditing) {
                    Button(
                        onClick = {
                            if (isBirthdayValid) {
                                val avatarModel = avatarUri?.let { uri ->
                                    val base64Image = encodeImageToBase64(uri, context)
                                    AvatarModel(
                                        filename = "avatar.png",
                                        base64 = base64Image
                                    )
                                }

                                onUpdateProfile(
                                    UpdateProfileModel(
                                        name = editableName,
                                        username = username,
                                        birthday = editableBirthday,
                                        city = editableCity,
                                        avatar = avatarModel
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

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Logout")
                }
            }
        }

        Button(
            onClick = {
                if (isEditing) {
                    editableName = username
                    editableCity = city
                    editableBirthday = birthday
                    avatarUri = null
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

// Функция для конвертации изображения в base64
@RequiresApi(Build.VERSION_CODES.O)
fun encodeImageToBase64(imageUri: Uri, context: android.content.Context): String? {
    val inputStream = context.contentResolver.openInputStream(imageUri)
    val bitmap = BitmapFactory.decodeStream(inputStream)
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    val byteArray = outputStream.toByteArray()
    return Base64.getEncoder().encodeToString(byteArray)
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

@RequiresApi(Build.VERSION_CODES.O)
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

