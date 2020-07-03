package com.dlet.cartrack.challenge.domain.usecase

import com.dlet.cartrack.challenge.domain.model.Country
import com.dlet.cartrack.challenge.domain.sealedclass.DataResult
import io.reactivex.Single

interface CountriesUseCase {
  fun getAllCountry(rawJson: String): Single<DataResult<List<Country>>>
}