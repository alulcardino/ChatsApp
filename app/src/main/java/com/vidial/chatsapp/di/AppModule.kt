package com.vidial.chatsapp.di

import com.vidial.chatsapp.data.repository.ChatRepositoryImpl
import com.vidial.chatsapp.domain.repository.ChatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideChatRepository() : ChatRepository {
        return ChatRepositoryImpl()
    }
}
