package com.vidial.chatsapp.domain.repository

import com.vidial.chatsapp.data.remote.dto.GetUserProfileDto
import com.vidial.chatsapp.data.remote.dto.UpdateProfileDto
import com.vidial.chatsapp.data.remote.requests.CodeRequest
import com.vidial.chatsapp.data.remote.requests.PhoneRequest
import com.vidial.chatsapp.data.remote.requests.RegisterRequest
import com.vidial.chatsapp.data.remote.response.AuthResponse

interface AuthRepository {
    suspend fun sendAuthCode(phoneRequest: PhoneRequest): Result<Unit>
    suspend fun checkAuthCode(codeRequest: CodeRequest): Result<AuthResponse>
    suspend fun registerUser(registerRequest: RegisterRequest): Result<AuthResponse>
    suspend fun getUserProfile(): Result<GetUserProfileDto>
    suspend fun updateUserProfile(profileRequest: UpdateProfileDto): Result<Unit>
    suspend fun logout()
}
