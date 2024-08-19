package com.vidial.chatsapp.di

import android.content.Context
import android.content.SharedPreferences
import com.vidial.chatsapp.data.remote.api.PlannerokApi
import com.vidial.chatsapp.data.remote.interceptor.AuthInterceptor
import com.vidial.chatsapp.data.repository.AuthRepositoryImpl
import com.vidial.chatsapp.domain.provider.TokenProvider
import com.vidial.chatsapp.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://plannerok.ru/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providePlannerokApi(retrofit: Retrofit): PlannerokApi {
        return retrofit.create(PlannerokApi::class.java)
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideAuthInterceptor(tokenProvider: TokenProvider): AuthInterceptor {
        return AuthInterceptor(tokenProvider)
    }

    @Singleton
    @Provides
    fun provideTokenProvider(
        api: PlannerokApi,
        preferences: SharedPreferences
    ): TokenProvider {
        return TokenProvider(api, preferences)
    }

    @Singleton
    @Provides
    fun provideAuthRepository(
        api: PlannerokApi,
        preferences: SharedPreferences
    ): AuthRepository {
        return AuthRepositoryImpl(api, preferences)
    }
}
