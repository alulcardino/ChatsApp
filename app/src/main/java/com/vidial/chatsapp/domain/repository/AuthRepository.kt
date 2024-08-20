package com.vidial.chatsapp.domain.repository

import com.vidial.chatsapp.data.remote.dto.UpdateProfileRequest
import com.vidial.chatsapp.data.remote.dto.UserProfile
import com.vidial.chatsapp.data.remote.requests.AuthResult

interface AuthRepository {
    suspend fun sendAuthCode(phone: String): Result<Unit>
    suspend fun checkAuthCode(phone: String, code: String): Result<AuthResult>
    suspend fun registerUser(phone: String, name: String, username: String): Result<AuthResult>
    suspend fun getUserProfile(): Result<UserProfile>
    suspend fun updateUserProfile(profileRequest: UpdateProfileRequest): Result<Unit>
    suspend fun logout()
}
