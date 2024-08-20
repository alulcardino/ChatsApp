package com.vidial.chatsapp.domain.usecase

import com.vidial.chatsapp.data.remote.dto.UserProfile
import com.vidial.chatsapp.domain.repository.AuthRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<UserProfile> {
        return authRepository.getUserProfile()
    }
}
