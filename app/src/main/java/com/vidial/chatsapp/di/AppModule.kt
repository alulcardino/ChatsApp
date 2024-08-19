package com.vidial.chatsapp.di

import com.vidial.chatsapp.data.remote.api.PlannerokApi
import com.vidial.chatsapp.data.repository.AuthRepositoryImpl
import com.vidial.chatsapp.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideAuthRepository(
        api: PlannerokApi
    ): AuthRepository {
        return AuthRepositoryImpl(api)
    }
}
