package com.vidial.chatsapp.data.remote.interceptor

import android.content.SharedPreferences
import android.util.Log
import com.vidial.chatsapp.domain.provider.TokenProvider
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val preferences: SharedPreferences
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url

        Log.d("AuthDebug", "Intercepting request: ${originalRequest.method} $url")
        Log.d("AuthDebug", "Original Headers: ${originalRequest.headers}")

        // Исключаем запросы, которые не требуют авторизации
        if (url.encodedPath.contains("/api/v1/users/register/") ||
            url.encodedPath.contains("/api/v1/users/send-auth-code/") ||
            url.encodedPath.contains("/api/v1/users/check-auth-code/")
        ) {
            Log.d("AuthDebug", "No authorization required for this request.")
            return chain.proceed(originalRequest)
        }

        // Извлекаем токен доступа
        val accessToken = preferences.getString(TokenProvider.TokenConstants.ACCESS_TOKEN_KEY, "")
        Log.d("AuthDebug", "Access Token: $accessToken")

        // Создаем запрос с заголовком авторизации
        val requestWithAuth = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        Log.d("AuthDebug", "Request with Auth Headers: ${requestWithAuth.headers}")

        // Выполняем запрос
        val response = chain.proceed(requestWithAuth)
        Log.d("AuthDebug", "Response Code: ${response.code}")

        return response
    }
}
