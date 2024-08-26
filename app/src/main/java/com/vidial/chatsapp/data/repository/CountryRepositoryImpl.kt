package com.vidial.chatsapp.data.repository

import com.vidial.chatsapp.R
import com.vidial.chatsapp.data.remote.dto.CountryDto
import com.vidial.chatsapp.domain.repository.CountryRepository
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor() : CountryRepository {
    private val countries = listOf(
        CountryDto("Kazakhstan", "KZ", "+7", R.drawable.flag_kz),
        CountryDto("Russia", "RU", "+7", R.drawable.flag_ru)
    )

    override fun getCountries(): List<CountryDto> {
        return countries
    }

    override fun getCountryByCode(code: String): CountryDto? {
        return countries.find { it.code == code }
    }
}

