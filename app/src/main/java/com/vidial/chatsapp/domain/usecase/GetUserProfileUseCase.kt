package com.vidial.chatsapp.domain.usecase

import com.vidial.chatsapp.data.remote.dto.GetUserProfileDto
import com.vidial.chatsapp.data.remote.dto.toUserProfileModel
import com.vidial.chatsapp.domain.model.UserProfileModel
import com.vidial.chatsapp.domain.repository.AuthRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<UserProfileModel> {
        return authRepository.getUserProfile().mapCatching { userProfileDto: GetUserProfileDto ->
            userProfileDto.toUserProfileModel()
        }
    }
}
