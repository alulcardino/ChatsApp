package com.vidial.chatsapp.presentation.ui.screens.chatList

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.vidial.chatsapp.R
import com.vidial.chatsapp.domain.model.ChatInfoModel
import com.vidial.chatsapp.presentation.ui.components.CoilImage

@Composable
fun ChatListScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
    viewModel: ChatListViewModel = hiltViewModel()
) {
    val chats by viewModel.chats.collectAsState()  // Collecting the state flow

    LazyColumn(
        contentPadding = paddingValues,
        modifier = Modifier.fillMaxSize()
    ) {
        items(chats) { chat ->
            ChatItem(chat = chat, onClick = {
                // Handle chat item click, navigate to details or something else
                navController.navigate("chatDetail/${chat.id}")
            })
        }
    }
}

@Composable
fun ChatItem(chat: ChatInfoModel, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = onClick)
    ) {
        // Replace with actual image loading mechanism (Coil, Glide etc.)
        CoilImage(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            imageUrl = chat.imageUrl,
            contentDescription = "",
            defaultImageResId = R.drawable.chat_default
        )

        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = chat.name)
            Text(text = chat.description)
        }
    }
}
