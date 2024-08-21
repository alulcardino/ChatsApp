package com.vidial.chatsapp.data.remote.requests

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("phone")
    val phone: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("username")
    val username: String)

