package com.vidial.chatsapp.domain.usecase

import com.vidial.chatsapp.data.remote.requests.AuthResult
import com.vidial.chatsapp.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(phoneNumber: String, name: String, username: String): Result<AuthResult> {
        return authRepository.registerUser(phoneNumber, name, username)
    }
}
