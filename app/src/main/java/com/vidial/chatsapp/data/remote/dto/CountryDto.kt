package com.vidial.chatsapp.data.remote.dto

import com.vidial.chatsapp.domain.model.CountryModel

data class CountryDto(
    val name: String,
    val code: String,
    val mobileCode: String,
    val flagResId: Int
)

fun CountryDto.toCountryModel() : CountryModel {
    return CountryModel(
        name, code, mobileCode, flagResId
    )
}
