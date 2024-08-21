package com.vidial.chatsapp.domain.repository

import com.vidial.chatsapp.data.remote.dto.GetUserProfileDto
import com.vidial.chatsapp.data.remote.dto.UpdateProfileDto
import com.vidial.chatsapp.data.remote.requests.AuthResult

interface AuthRepository {
    suspend fun sendAuthCode(phone: String): Result<Unit>
    suspend fun checkAuthCode(phone: String, code: String): Result<AuthResult>
    suspend fun registerUser(phone: String, name: String, username: String): Result<AuthResult>
    suspend fun getUserProfile(): Result<GetUserProfileDto>
    suspend fun updateUserProfile(profileRequest: UpdateProfileDto): Result<Unit>
    suspend fun logout()
}
