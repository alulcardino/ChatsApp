    package com.vidial.chatsapp.presentation.ui.screens

    import android.util.Log
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.vidial.chatsapp.domain.model.UpdateProfileModel
    import com.vidial.chatsapp.domain.model.UserProfileModel
    import com.vidial.chatsapp.domain.usecase.GetUserProfileUseCase
    import com.vidial.chatsapp.domain.usecase.LogoutUseCase
    import com.vidial.chatsapp.domain.usecase.chat.UpdateUserProfileUseCase
    import dagger.hilt.android.lifecycle.HiltViewModel
    import kotlinx.coroutines.flow.MutableStateFlow
    import kotlinx.coroutines.flow.StateFlow
    import kotlinx.coroutines.launch
    import javax.inject.Inject

    sealed class UserProfileState {
        object Loading : UserProfileState()
        data class Success(val userProfile: UserProfileModel) : UserProfileState()
        data class Error(val message: String) : UserProfileState()
    }


    @HiltViewModel
    class UserProfileViewModel @Inject constructor(
        private val getUserProfileUseCase: GetUserProfileUseCase,
        private val updateUserProfileUseCase: UpdateUserProfileUseCase,
        private val logoutUseCase: LogoutUseCase  // Новый use case для logout
    ) : ViewModel() {

        private val _userProfileState = MutableStateFlow<UserProfileState>(UserProfileState.Loading)
        val userProfileState: StateFlow<UserProfileState> = _userProfileState

        init {
            fetchUserProfile()
        }

        private fun fetchUserProfile() {
            viewModelScope.launch {
                _userProfileState.value = UserProfileState.Loading
                val result = getUserProfileUseCase()
                _userProfileState.value = when {
                    result.isSuccess -> {
                        val userProfile = result.getOrNull()
                        if (userProfile != null) {
                            Log.d("UserProfileViewModel", "Fetched user profile: $userProfile")
                            UserProfileState.Success(userProfile)
                        } else {
                            Log.e("UserProfileViewModel", "User profile is null")
                            UserProfileState.Error("User profile is null")
                        }
                    }
                    result.isFailure -> {
                        Log.e("UserProfileViewModel", "Failed to fetch user profile")
                        val message = result.exceptionOrNull()?.message ?: "Unknown error"
                        UserProfileState.Error(message)
                    }
                    else -> {
                        UserProfileState.Error("Unknown error")
                    }
                }
            }
        }

        fun updateProfile(profileModel: UpdateProfileModel) {
            viewModelScope.launch {
                val result = updateUserProfileUseCase(profileModel)
                if (result.isSuccess) {
                    fetchUserProfile()
                } else {
                    val message = result.exceptionOrNull()?.message ?: "Unknown error"
                    _userProfileState.value = UserProfileState.Error(message)
                }
            }
        }

        fun logout() {
            viewModelScope.launch {
                logoutUseCase()
                _userProfileState.value = UserProfileState.Loading
            }
        }
    }
