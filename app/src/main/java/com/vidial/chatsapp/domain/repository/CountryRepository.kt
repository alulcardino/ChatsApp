package com.vidial.chatsapp.domain.repository

import com.vidial.chatsapp.data.remote.dto.CountryDto

interface CountryRepository {
    fun getCountries(): List<CountryDto>
    fun getCountryByCode(code: String): CountryDto?
}
