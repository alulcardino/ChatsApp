package com.vidial.chatsapp.data.remote.requests

data class AuthResult(
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
    val isUserExists: Boolean
)
