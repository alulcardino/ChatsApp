package com.vidial.chatsapp.data.repository

import com.vidial.chatsapp.data.remote.dto.ChatDto
import com.vidial.chatsapp.domain.repository.ChatRepository
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor() : ChatRepository {

    private val chats = listOf(
        ChatDto(
            id = 1,
            imageUrl = "https://cdn.pixabay.com/photo/2020/02/10/14/52/cat-4835017_1280.jpg",
            name = "Cat Lovers",
            description = "A chat for people who love cats. Share your cat pictures and stories!"
        ),
        ChatDto(
            id = 2,
            imageUrl = "https://cdn.pixabay.com/photo/2016/03/27/07/08/earth-1283625_1280.jpg",
            name = "Earth Explorers",
            description = "Discuss the latest in travel, nature, and exploration."
        ),
        ChatDto(
            id = 3,
            imageUrl = "https://cdn.pixabay.com/photo/2014/12/27/15/40/hacker-581636_1280.jpg",
            name = "Tech Geeks",
            description = "All about technology, programming, and the latest gadgets."
        ),
        ChatDto(
            id = 4,
            imageUrl = "https://cdn.pixabay.com/photo/2017/08/10/03/28/people-2616670_1280.jpg",
            name = "Fitness Group",
            description = "A group for fitness enthusiasts. Share tips, workouts, and progress!"
        ),
        ChatDto(
            id = 5,
            imageUrl = "https://cdn.pixabay.com/photo/2016/11/29/12/54/book-1868068_1280.jpg",
            name = "Book Club",
            description = "Discuss the latest books you've read and get recommendations."
        ),
        ChatDto(
            id = 6,
            imageUrl = "https://cdn.pixabay.com/photo/2017/08/07/08/56/bike-2608478_1280.jpg",
            name = "Cycling Enthusiasts",
            description = "For those who love cycling, from casual riders to hardcore racers."
        ),
        ChatDto(
            id = 7,
            imageUrl = "https://cdn.pixabay.com/photo/2015/03/26/09/39/coffee-690245_1280.jpg",
            name = "Coffee Lovers",
            description = "All about coffee - from beans to brewing methods, and everything in between."
        ),
        ChatDto(
            id = 8,
            imageUrl = "https://cdn.pixabay.com/photo/2016/01/19/17/52/wind-turbine-1149604_1280.jpg",
            name = "Green Energy",
            description = "Discuss renewable energy, sustainability, and how to make a positive impact."
        ),
        ChatDto(
            id = 9,
            imageUrl = "https://cdn.pixabay.com/photo/2016/01/09/18/31/coding-1137372_1280.jpg",
            name = "Coding Community",
            description = "For coders of all levels. Share projects, ask for help, and improve your skills."
        ),
        ChatDto(
            id = 10,
            imageUrl = "https://cdn.pixabay.com/photo/2014/12/15/17/16/bitcoin-569621_1280.jpg",
            name = "Crypto Enthusiasts",
            description = "Discuss the latest trends in cryptocurrency and blockchain technology."
        )
    )

    override suspend fun getChats(): List<ChatDto> {
        return chats
    }


    override suspend fun getChatById(id: Int): ChatDto {
        return chats.find { it.id == id }
            ?: throw NoSuchElementException("Chat with id $id not found")
    }
}
