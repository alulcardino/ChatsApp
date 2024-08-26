package com.vidial.chatsapp.presentation.ui.screens.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.vidial.chatsapp.R
import com.vidial.chatsapp.domain.model.AvatarModel
import com.vidial.chatsapp.domain.model.UpdateProfileModel
import com.vidial.chatsapp.domain.model.UserProfileModel
import com.vidial.chatsapp.presentation.ui.components.ChatButton
import com.vidial.chatsapp.presentation.ui.components.ChatsAppBar
import com.vidial.chatsapp.presentation.ui.components.CoilImage
import com.vidial.chatsapp.presentation.ui.components.ErrorScreen
import com.vidial.chatsapp.presentation.ui.components.LoadingScreen
import com.vidial.chatsapp.presentation.ui.components.navigation.ScreenRoute
import com.vidial.chatsapp.presentation.ui.theme.DarkPurple
import com.vidial.chatsapp.presentation.ui.theme.DeepBlue
import com.vidial.chatsapp.presentation.ui.theme.LightGray
import com.vidial.chatsapp.presentation.ui.theme.LightPurple

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
    val editableStatus by viewModel.editableStatus.collectAsState()
    val effectFlow = viewModel.effect

    val context = LocalContext.current

    LaunchedEffect(effectFlow) {
        effectFlow.collect { effect ->
            when (effect) {
                is UserProfileEffect.NavigateToPhoneNumberScreen -> navController.navigate(ScreenRoute.PhoneNumberScreen.route) { popUpTo(0) }
                is UserProfileEffect.ShowErrorMessage -> Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                is UserProfileEffect.ProfileUpdated -> Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                is UserProfileEffect.NavigateBack -> navController.popBackStack()
            }
        }
    }

    Scaffold(
        topBar = {
            ChatsAppBar(
                title = stringResource(R.string.profile),
                navigationIcon = Icons.Default.ArrowBack,
                actionIcon = Icons.Default.Edit,
                onNavigationClick = { viewModel.navigateBack() },
                onActionClick = { viewModel.handleIntent(UserProfileIntent.StartEditing) }
            )
        },
        content = { innerPadding ->
            when (state) {
                is UserProfileState.Loading -> LoadingScreen(innerPadding)
                is UserProfileState.Success -> UserProfileContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    viewModel = viewModel,
                    userProfile = (state as UserProfileState.Success).userProfile,
                    isEditing = isEditing,
                    avatarUri = avatarUri,
                    editableName = editableName,
                    editableCity = editableCity,
                    editableBirthday = editableBirthday,
                    editableStatus = editableStatus,
                    onEditProfile = { viewModel.handleIntent(UserProfileIntent.StartEditing) },
                    onCancelEdit = { viewModel.handleIntent(UserProfileIntent.CancelEditing) },
                    onUpdateProfile = { updatedProfile -> viewModel.handleIntent(UserProfileIntent.UpdateProfile(updatedProfile)) },
                    onLogout = { viewModel.handleIntent(UserProfileIntent.Logout) },
                    onAvatarSelected = { uri -> viewModel.handleIntent(UserProfileIntent.SetAvatarUri(uri)) },
                    onBack = { viewModel.navigateBack() }
                )
                is UserProfileState.Error -> ErrorScreen((state as UserProfileState.Error).message, innerPadding)
            }
        }
    )
}


@Composable
fun UserProfileContent(
    modifier: Modifier,
    viewModel: UserProfileViewModel,
    userProfile: UserProfileModel,
    isEditing: Boolean,
    avatarUri: Uri?,
    editableName: String,
    editableCity: String,
    editableBirthday: String,
    editableStatus: String,
    onEditProfile: () -> Unit,
    onCancelEdit: () -> Unit,
    onUpdateProfile: (UpdateProfileModel) -> Unit,
    onLogout: () -> Unit,
    onAvatarSelected: (Uri) -> Unit,
    onBack: () -> Unit
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

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AvatarSection(
            avatarUri = avatarUri,
            avatarUrl = userProfile.avatarUrl,
            isEditing = isEditing,
            onClick = {
                val pickImageIntent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
                imagePickerLauncher.launch(pickImageIntent)
            }
        )
        EditableTextField(
            label = stringResource(R.string.username),
            value = editableName,
            onValueChange = { },
            isEditing = isEditing,
            modifier = Modifier.fillMaxWidth()
        )
        EditableTextField(
            label = stringResource(R.string.phone),
            value = userProfile.phone,
            onValueChange = {},
            isEditing = false,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            EditableTextField(
                label = stringResource(R.string.birth_date),
                value = editableBirthday,
                onValueChange = { newBirthday -> viewModel.updateEditableBirthday(newBirthday) },
                isEditing = isEditing,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                errorMessage = if (isEditing && !isBirthdayValid) "Format is dd.mm.yyyy" else null,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            EditableTextField(
                label = stringResource(R.string.zodiac_sign),
                value = userProfile.zodiacSign,
                onValueChange = {},
                isEditing = false,
                modifier = Modifier.weight(1f)
            )
        }


        // Поле города
        EditableTextField(
            label = stringResource(R.string.city),
            value = editableCity,
            onValueChange = { newCity -> viewModel.updateEditableCity(newCity) },
            isEditing = isEditing,
            modifier = Modifier.fillMaxWidth()
        )


        EditableTextField(
            label = stringResource(R.string.about),
            value = editableStatus,
            onValueChange = { newAbout -> viewModel.updateEditableStatus(newAbout) },
            isEditing = isEditing,
            modifier = Modifier.fillMaxWidth()
        )


        if (isEditing) {
            SaveButton(
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
                                status = editableStatus,
                                avatar = avatarModel
                            )
                        )
                    }
                },
                enabled = isBirthdayValid
            )
            ChatButton(onClick = onCancelEdit, text = stringResource(R.string.cancel))
        } else {
            ChatButton(onClick = onLogout, text = stringResource(R.string.logout))
        }
    }
}

@Composable
fun EditableTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isEditing: Boolean,
    isRequired: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    errorMessage: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
    ) {
        TextField(
            value = value,
            onValueChange = {
                val formattedValue = visualTransformation.filter(AnnotatedString(it)).text.text
                onValueChange(formattedValue)
            },
            label = {
                Row {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    if (isRequired) {
                        Text(
                            text = " *",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = if (isEditing) LightPurple else LightGray,
                unfocusedContainerColor = if (isEditing) LightPurple else LightGray,
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
                disabledLabelColor = DarkPurple
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium),
            enabled = isEditing,
        )
        Spacer(modifier = Modifier.height(8.dp))
        errorMessage?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun AvatarSection(
    modifier: Modifier = Modifier,
    avatarUri: Uri?,
    avatarUrl: String?,
    isEditing: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(100.dp)
            .clip(MaterialTheme.shapes.medium)
            .padding(16.dp)
            .clickable { if (isEditing) onClick() },
        contentAlignment = Alignment.Center
    ) {
        CoilImage(
            imageUrl = avatarUri?.toString() ?: avatarUrl,
            defaultImageResId = R.drawable.avatar_placeholder,
            contentDescription = "Avatar",
            modifier = Modifier
                .size(80.dp)
                .clip(MaterialTheme.shapes.medium)
        )
    }
}

@Composable
fun SaveButton(
    onClick: () -> Unit,
    enabled: Boolean
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = DeepBlue)
    ) {
        Text(stringResource(R.string.save), color = Color.White)
    }
}

