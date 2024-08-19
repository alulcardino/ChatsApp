package com.vidial.chatsapp.data.remote.api

import com.vidial.chatsapp.data.remote.requests.CodeRequest
import com.vidial.chatsapp.data.remote.requests.PhoneRequest
import com.vidial.chatsapp.data.remote.response.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PlannerokApi {
    @POST("/api/v1/users/send-auth-code/")
    suspend fun sendAuthCode(@Body request: PhoneRequest): Response<Unit>

    @POST("/api/v1/users/check-auth-code/")
    suspend fun checkAuthCode(@Body request: CodeRequest): Response<AuthResponse>
}
