package com.vidial.chatsapp.presentation.ui.screens.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
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
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Base64
import java.util.Locale

@Composable
fun ProfileScreen(
    viewModel: UserProfileViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val state by viewModel.userProfileState.collectAsState()
    val isEditing by viewModel.isEditing.collectAsState()
    val avatarUri by viewModel.avatarUri.collectAsState()
    val editableName by viewModel.editableName.collectAsState()
    val editableCity by viewModel.editableCity.collectAsState()
    val editableBirthday by viewModel.editableBirthday.collectAsState()
    val effectFlow = viewModel.effect

    val context = LocalContext.current

    LaunchedEffect(effectFlow) {
        effectFlow.collect { effect ->
            when (effect) {
                is UserProfileEffect.NavigateToPhoneNumberScreen -> {
                    navController.navigate(ScreenRoute.PhoneNumberScreen.route) {
                        popUpTo(0)
                    }
                }
                is UserProfileEffect.ShowErrorMessage -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                is UserProfileEffect.ProfileUpdated -> {
                    Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    when (state) {
        is UserProfileState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is UserProfileState.Success -> {
            val userProfile = (state as UserProfileState.Success).userProfile
            UserProfileContent(
                viewModel = viewModel,
                userProfile = userProfile,
                isEditing = isEditing,
                avatarUri = avatarUri,
                editableName = editableName,
                editableCity = editableCity,
                editableBirthday = editableBirthday,
                onEditProfile = { viewModel.handleIntent(UserProfileIntent.StartEditing) },
                onCancelEdit = { viewModel.handleIntent(UserProfileIntent.CancelEditing) },
                onUpdateProfile = { updatedProfile -> viewModel.handleIntent(UserProfileIntent.UpdateProfile(updatedProfile)) },
                onLogout = { viewModel.handleIntent(UserProfileIntent.Logout) },
                onAvatarSelected = { uri -> viewModel.handleIntent(UserProfileIntent.SetAvatarUri(uri)) }
            )
        }

        is UserProfileState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = (state as UserProfileState.Error).message, color = MaterialTheme.colorScheme.error, fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun UserProfileContent(
    viewModel: UserProfileViewModel,
    userProfile: UserProfileModel,
    isEditing: Boolean,
    avatarUri: Uri?,
    editableName: String,
    editableCity: String,
    editableBirthday: String,
    onEditProfile: () -> Unit,
    onCancelEdit: () -> Unit,
    onUpdateProfile: (UpdateProfileModel) -> Unit,
    onLogout: () -> Unit,
    onAvatarSelected: (Uri) -> Unit
) {
    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri -> onAvatarSelected(uri) }
        }
    }

    val isBirthdayValid = viewModel.isDateValid(editableBirthday)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            AvatarSection(
                avatarUri = avatarUri,
                avatarUrl = userProfile.avatarUrl,
                isEditing = isEditing,
                onClick = {
                    val pickImageIntent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
                    imagePickerLauncher.launch(pickImageIntent)
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            EditableInfoSection(
                label = "Nickname",
                value = userProfile.username
            )
            EditableInfoSection(
                label = "Phone",
                value = userProfile.phone
            )
            EditableTextField(
                label = "City",
                value = editableCity,
                onValueChange = { newCity -> viewModel.updateEditableCity(newCity) },
                isEditing = isEditing
            )
            EditableTextField(
                label = "Birth Date",
                value = editableBirthday,
                onValueChange = { newBirthday -> viewModel.updateEditableBirthday(newBirthday) },
                isEditing = isEditing,
                isRequired = true
            )

            if (isEditing && !isBirthdayValid) {
                Text(
                    text = "Birth Date must be in the format dd.MM.yyyy",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            EditableInfoSection(
                label = "Zodiac Sign",
                value = userProfile.zodiacSign
            )

            if (isEditing) {
                Button(
                    onClick = {
                        if (isBirthdayValid) {
                            val avatarModel = avatarUri?.let { uri ->
                                val base64Image = viewModel.encodeImageToBase64(uri, context)
                                AvatarModel(filename = "avatar.png", base64 = base64Image)
                            }
                            onUpdateProfile(
                                UpdateProfileModel(
                                    name = editableName,
                                    username = userProfile.username,
                                    birthday = editableBirthday,
                                    city = editableCity,
                                    avatar = avatarModel
                                )
                            )
                        }
                    },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    enabled = isBirthdayValid
                ) {
                    Text("Save")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onCancelEdit,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel")
                }
            } else {
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Logout")
                }
            }
        }
    }

    EditButton(isEditing, onEditProfile, onCancelEdit)
}


@Composable
fun AvatarSection(
    avatarUri: Uri?,
    avatarUrl: String?,
    isEditing: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
            .padding(4.dp)
            .clickable(enabled = isEditing, onClick = onClick)
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
}

@Composable
fun EditableTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isEditing: Boolean,
    isRequired: Boolean = false
) {
    if (isEditing) {
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
    } else {
        EditableInfoSection(label, value)
    }
}

@Composable
fun EditableInfoSection(
    label: String,
    value: String
) {
    Text("$label: $value")
}

@Composable
fun EditButton(
    isEditing: Boolean,
    onEditProfile: () -> Unit,
    onCancelEdit: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {
                if (isEditing) onCancelEdit() else onEditProfile()
            }
        ) {
            Text(if (isEditing) "Cancel" else "Edit")
        }
    }
}
