package com.vidial.chatsapp.data.remote.requests

import com.google.gson.annotations.SerializedName

data class PhoneRequest(
    @SerializedName("phone")
    val phone: String
)
