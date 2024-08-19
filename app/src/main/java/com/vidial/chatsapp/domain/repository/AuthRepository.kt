package com.vidial.chatsapp.domain.repository

import com.vidial.chatsapp.data.remote.response.AuthResponse

interface AuthRepository {
    suspend fun sendAuthCode(phone: String): Result<Unit>
    suspend fun checkAuthCode(phone: String, code: String): Result<AuthResponse>
}

