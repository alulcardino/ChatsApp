package com.vidial.chatsapp.data.repository

import com.vidial.chatsapp.data.remote.dto.ChatDto
import com.vidial.chatsapp.data.remote.dto.MessageDto
import com.vidial.chatsapp.domain.repository.ChatRepository
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor() : ChatRepository {

    private val chats = listOf(
        ChatDto(
            id = 1,
            imageUrl = "https://i.pravatar.cc/150?img=1",
            name = "Cat Lovers",
            description = "A chat for people who love cats. Share your cat pictures and stories!"
        ),
        ChatDto(
            id = 2,
            imageUrl = "https://i.pravatar.cc/150?img=2",
            name = "Earth Explorers",
            description = "Discuss the latest in travel, nature, and exploration."
        ),
        ChatDto(
            id = 3,
            imageUrl = "https://i.pravatar.cc/150?img=3",
            name = "Tech Geeks",
            description = "All about technology, programming, and the latest gadgets."
        ),
        ChatDto(
            id = 4,
            imageUrl = "https://i.pravatar.cc/150?img=4",
            name = "Fitness Group",
            description = "A group for fitness enthusiasts. Share tips, workouts, and progress!"
        ),
        ChatDto(
            id = 5,
            imageUrl = "https://i.pravatar.cc/150?img=5",
            name = "Book Club",
            description = "Discuss the latest books you've read and get recommendations."
        ),
        ChatDto(
            id = 6,
            imageUrl = "https://i.pravatar.cc/150?img=6",
            name = "Cycling Enthusiasts",
            description = "For those who love cycling, from casual riders to hardcore racers."
        ),
        ChatDto(
            id = 7,
            imageUrl = "https://i.pravatar.cc/150?img=7",
            name = "Coffee Lovers",
            description = "All about coffee - from beans to brewing methods, and everything in between."
        ),
        ChatDto(
            id = 8,
            imageUrl = "https://i.pravatar.cc/150?img=8",
            name = "Green Energy",
            description = "Discuss renewable energy, sustainability, and how to make a positive impact."
        ),
        ChatDto(
            id = 9,
            imageUrl = "https://i.pravatar.cc/150?img=9",
            name = "Coding Community",
            description = "For coders of all levels. Share projects, ask for help, and improve your skills."
        ),
        ChatDto(
            id = 10,
            imageUrl = "https://i.pravatar.cc/150?img=10",
            name = "Crypto Enthusiasts",
            description = "Discuss the latest trends in cryptocurrency and blockchain technology."
        )
    )

    private val messages = listOf(
        MessageDto(
            id = 1,
            chatId = 1,
            sender = "Alice",
            content = "Look at my cat!",
            timestamp = "2024-08-20 10:00:00"
        ),
        MessageDto(
            id = 2,
            chatId = 1,
            sender = "Bob",
            content = "So cute!",
            timestamp = "2024-08-20 10:01:00"
        ),
        MessageDto(
            id = 3,
            chatId = 2,
            sender = "Charlie",
            content = "Has anyone been to the Himalayas?",
            timestamp = "2024-08-20 11:00:00"
        )
        // Добавьте больше сообщений по необходимости
    )

    override suspend fun getChats(): List<ChatDto> {
        return chats
    }

    override suspend fun getChatById(id: Int): ChatDto {
        return chats.find { it.id == id }
            ?: throw NoSuchElementException("Chat with id $id not found")
    }

    override suspend fun getMessagesForChat(chatId: Int): List<MessageDto> {
        return messages.filter { it.chatId == chatId }
    }
}
