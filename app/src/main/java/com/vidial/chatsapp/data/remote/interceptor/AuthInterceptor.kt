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

        // Логируем исходный запрос
        Log.d("AuthDebug", "Original Request: ${originalRequest.method} ${url}")
        Log.d("AuthDebug", "Headers: ${originalRequest.headers}")

        // Логируем тело запроса (если это POST или PUT)
        if (originalRequest.body != null) {
            val buffer = Buffer()
            originalRequest.body?.writeTo(buffer)
            Log.d("AuthDebug", "Request Body: ${buffer.readUtf8()}")
        }

        // Пропускаем запрос без авторизации, если это регистрация или отправка кода
        if (url.encodedPath.contains("/api/v1/users/register/") ||
            url.encodedPath.contains("/api/v1/users/send-auth-code/") ||
            url.encodedPath.contains("/api/v1/users/check-auth-code/")
        ) {
            Log.d("AuthDebug", "Intercepting non-auth request: ${originalRequest.url}")
            return chain.proceed(originalRequest)
        }

        // Извлечение токена из SharedPreferences
        val accessToken = preferences.getString(TokenProvider.TokenConstants.ACCESS_TOKEN_KEY, "") ?: ""
        Log.d("AuthDebug", "Access Token: $accessToken")

        if (accessToken.isEmpty()) {
            Log.d("AuthDebug", "No access token found. Proceeding without auth.")
            return chain.proceed(originalRequest)
        }

        val requestWithAuth = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        Log.d("AuthDebug", "Request with Auth: ${requestWithAuth.method} ${requestWithAuth.url}")
        Log.d("AuthDebug", "Headers: ${requestWithAuth.headers}")

        // Логируем тело запроса (если это POST или PUT)
        if (requestWithAuth.body != null) {
            val buffer = Buffer()
            requestWithAuth.body?.writeTo(buffer)
            Log.d("AuthDebug", "Request Body with Auth: ${buffer.readUtf8()}")
        }

        val response = chain.proceed(requestWithAuth)

        // Обработка 401 Unauthorized
        if (response.code == 401) {
            Log.d("AuthDebug", "401 Unauthorized response.")
            // Обработку обновления токена можно оставить на уровне репозитория
        }

        return response
    }
}
