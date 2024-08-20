package com.vidial.chatsapp.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.vidial.chatsapp.data.remote.dto.UserProfile

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
            UserProfileContent(userProfile)
        }
        is UserProfileState.Error -> {
            val message = (state as UserProfileState.Error).message
            // Показать сообщение об ошибке
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = message, color = MaterialTheme.colorScheme.error, fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun UserProfileContent(userProfile: UserProfile) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            // Аватарка
            AsyncImage(
                model = userProfile.avatarUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Информация о пользователе
            Text(text = "Nickname: ${userProfile.nickname}", style = MaterialTheme.typography.titleLarge)
            Text(text = "Phone: ${userProfile.phoneNumber}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "City: ${userProfile.city}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Birth Date: ${userProfile.birthDate}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Zodiac Sign: ${userProfile.zodiacSign}", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "About: ${userProfile.about}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
