package com.vidial.chatsapp.data.remote.api

import com.vidial.chatsapp.data.remote.dto.UpdateProfileDto
import com.vidial.chatsapp.data.remote.dto.GetUserProfileDto
import com.vidial.chatsapp.data.remote.response.AuthResponse
import com.vidial.chatsapp.data.remote.requests.CodeRequest
import com.vidial.chatsapp.data.remote.requests.PhoneRequest
import com.vidial.chatsapp.data.remote.requests.RefreshTokenRequest
import com.vidial.chatsapp.data.remote.response.RefreshTokenResponse
import com.vidial.chatsapp.data.remote.requests.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface PlannerokApi {
    @POST("/api/v1/users/send-auth-code/")
    suspend fun sendAuthCode(@Body request: PhoneRequest): Response<Unit>

    @POST("/api/v1/users/check-auth-code/")
    suspend fun checkAuthCode(@Body request: CodeRequest): Response<AuthResponse>

    @POST("/api/v1/users/refresh-token/")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<RefreshTokenResponse>

    @POST("/api/v1/users/register/")
    suspend fun registerUser(@Body request: RegisterRequest): Response<AuthResponse>

    @GET("/api/v1/users/me/")
    suspend fun getUserProfile(): Response<GetUserProfileDto>

    @PUT("/api/v1/users/me/")
    suspend fun updateProfile(@Body request: UpdateProfileDto): Response<Unit>
}
