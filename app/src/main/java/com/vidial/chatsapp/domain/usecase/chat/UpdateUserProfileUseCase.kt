package com.vidial.chatsapp.domain.usecase.chat

import com.vidial.chatsapp.domain.model.UpdateProfileModel
import com.vidial.chatsapp.domain.model.toUpdateProfileDto
import com.vidial.chatsapp.domain.repository.AuthRepository
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(profileRequest: UpdateProfileModel): Result<Unit> {
        return authRepository.updateUserProfile(profileRequest.toUpdateProfileDto())
    }
}
