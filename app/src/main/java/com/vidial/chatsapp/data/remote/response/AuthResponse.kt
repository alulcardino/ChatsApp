package com.vidial.chatsapp.data.remote.response

data class AuthResponse(
    val refreshToken: String,
    val accessToken: String,
    val userId: String,
    val isUserExists: Boolean
)
