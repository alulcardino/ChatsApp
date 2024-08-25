package com.vidial.chatsapp.presentation.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.vidial.chatsapp.presentation.ui.components.ChatsAppBar

@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel(),
    navController: NavHostController,
    chatId: Int
) {
    val state by viewModel.state.collectAsState()
    var messageContent by remember { mutableStateOf("") }

    LaunchedEffect(chatId) {
        viewModel.handleIntent(ChatIntent.LoadChat(chatId))
        viewModel.handleIntent(ChatIntent.LoadMessages(chatId))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ChatEffect.ShowError -> {
                }
                ChatEffect.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        topBar = { ChatsAppBar(title = "Chats") },
        //{ ChatTopBar(navController = navController, title = state.chat?.name ?: "Chat") },
        content = { innerPadding ->
            ChatContent(
                state = state,
                messageContent = messageContent,
                onMessageContentChange = { messageContent = it },
                onSendMessage = {
                    if (messageContent.isNotBlank()) {
                        viewModel.handleIntent(
                            ChatIntent.SendMessage(
                                chatId,
                                "You",
                                messageContent
                            )
                        )
                        messageContent = ""
                    }
                },
                innerPadding = innerPadding
            )
        }
    )
}



@Composable
fun ChatContent(
    state: ChatState, // Используем ChatState вместо ChatViewState
    messageContent: String,
    onMessageContentChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    innerPadding: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .imePadding()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            items(state.messages) { message ->
                MessageItem(
                    sender = message.sender,
                    content = message.content,
                    isCurrentUser = message.sender == "You"
                )
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        MessageInput(
            messageContent = messageContent,
            onMessageContentChange = onMessageContentChange,
            onSendMessage = onSendMessage
        )

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageInput(
    messageContent: String,
    onMessageContentChange: (String) -> Unit,
    onSendMessage: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = messageContent,
            onValueChange = onMessageContentChange,
            label = { Text("Type a message") },
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = onSendMessage) {
            Icon(
                Icons.Default.Send,
                contentDescription = "Send Message",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun MessageItem(sender: String, content: String, isCurrentUser: Boolean) {
    val backgroundColor =
        if (isCurrentUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.inversePrimary
    val textColor =
        if (isCurrentUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
    val shape: CornerBasedShape = if (isCurrentUser) {
        RoundedCornerShape(16.dp, 16.dp, 0.dp, 16.dp)
    } else {
        RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .clip(shape)
                .background(backgroundColor)
                .padding(12.dp)
        ) {
            Column {
                Text(
                    text = sender,
                    style = MaterialTheme.typography.labelSmall,
                    color = textColor
                )
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyLarge,
                    color = textColor
                )
            }
        }
    }
}

