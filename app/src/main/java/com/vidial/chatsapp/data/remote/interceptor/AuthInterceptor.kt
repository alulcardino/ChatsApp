package com.vidial.chatsapp.data.remote.interceptor

import com.vidial.chatsapp.domain.provider.TokenProvider
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenProvider: TokenProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url

        // Пропустить добавление заголовка Authorization для определенных эндпоинтов
        if (url.encodedPath.contains("/api/v1/users/register/") ||
            url.encodedPath.contains("/api/v1/users/send-auth-code/") ||
            url.encodedPath.contains("/api/v1/users/check-auth-code/")
        ) {
            return chain.proceed(originalRequest)
        }

        val accessToken = tokenProvider.getAccessToken()
        val requestWithAuth = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        val response = chain.proceed(requestWithAuth)

        if (response.code == 401) { // Unauthorized
            synchronized(this) {
                val newAccessToken = tokenProvider.refreshAccessToken()
                if (newAccessToken != null) {
                    // Повторно отправить исходный запрос с новым токеном
                    val requestWithNewAuth = originalRequest.newBuilder()
                        .header("Authorization", "Bearer $newAccessToken")
                        .build()
                    return chain.proceed(requestWithNewAuth)
                }
            }
        }

        return response
    }
}
