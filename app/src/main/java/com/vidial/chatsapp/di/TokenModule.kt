package com.vidial.chatsapp.di

import android.content.SharedPreferences
import com.vidial.chatsapp.data.remote.api.PlannerokApi
import com.vidial.chatsapp.domain.provider.TokenProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TokenModule {

    @Singleton
    @Provides
    fun provideTokenProvider(
        api: PlannerokApi,
        preferences: SharedPreferences
    ): TokenProvider {
        return TokenProvider(api, preferences)
    }
}
