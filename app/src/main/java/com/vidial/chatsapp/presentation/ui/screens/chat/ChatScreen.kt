package com.vidial.chatsapp.presentation.ui.screens.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.ui.Alignment


@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel(),
    paddingValues: PaddingValues,
    navController: NavHostController,
    chatId: Int
) {
    val chat by viewModel.chat.collectAsState()
    val messages by viewModel.messages.collectAsState()

    var messageContent by remember { mutableStateOf("") }

    LaunchedEffect(chatId) {
        viewModel.loadChat(chatId)
        viewModel.loadMessagesForChat(chatId)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        chat?.let {
            Text("Chat: ${it.name}")
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(messages) { message ->
                Text("${message.sender}: ${message.content}")
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = messageContent,
                onValueChange = { messageContent = it },
                label = { Text("Type a message") },
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = {
                    if (messageContent.isNotBlank()) {
                        viewModel.sendMessage(chatId, "You", messageContent)
                        messageContent = ""
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Send")
            }
        }
    }
}
