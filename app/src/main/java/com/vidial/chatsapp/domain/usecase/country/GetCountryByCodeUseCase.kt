package com.vidial.chatsapp.domain.usecase.country

import com.vidial.chatsapp.data.remote.mappers.toCountryModel
import com.vidial.chatsapp.domain.model.CountryModel
import com.vidial.chatsapp.domain.repository.CountryRepository
import javax.inject.Inject


class GetCountryByCodeUseCase @Inject constructor(
    private val countryRepository: CountryRepository
) {
    fun execute(code: String): CountryModel? {
        return countryRepository.getCountryByCode(code)?.toCountryModel()
    }
}
