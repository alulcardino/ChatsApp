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

        if (url.encodedPath.contains("/api/v1/users/register/") ||
            url.encodedPath.contains("/api/v1/users/send-auth-code/") ||
            url.encodedPath.contains("/api/v1/users/check-auth-code/")
        ) {
            return chain.proceed(originalRequest)
        }

        val accessToken = preferences.getString(TokenProvider.TokenConstants.ACCESS_TOKEN_KEY, "")

        val requestWithAuth = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        val response = chain.proceed(requestWithAuth)

        return response
    }
}
