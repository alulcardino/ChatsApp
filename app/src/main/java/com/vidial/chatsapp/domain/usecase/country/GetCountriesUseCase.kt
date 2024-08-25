package com.vidial.chatsapp.domain.usecase.country

import com.vidial.chatsapp.data.remote.mappers.toCountryModel
import com.vidial.chatsapp.domain.model.CountryModel
import com.vidial.chatsapp.domain.repository.AuthRepository
import com.vidial.chatsapp.domain.repository.CountryRepository
import javax.inject.Inject

class GetCountriesUseCase@Inject constructor(
    private val countryRepository: CountryRepository
) {
    fun execute(): List<CountryModel> {
        return countryRepository.getCountries().map { it.toCountryModel() }
    }
}
