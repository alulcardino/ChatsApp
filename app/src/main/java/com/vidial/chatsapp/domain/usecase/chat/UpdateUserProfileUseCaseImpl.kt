package com.vidial.chatsapp.domain.usecase.chat

import com.vidial.chatsapp.data.remote.dto.UpdateProfileRequest
import com.vidial.chatsapp.domain.repository.AuthRepository
import javax.inject.Inject



class UpdateUserProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(profileRequest: UpdateProfileRequest): Result<Unit> {
        return authRepository.updateUserProfile(profileRequest)
    }
}
