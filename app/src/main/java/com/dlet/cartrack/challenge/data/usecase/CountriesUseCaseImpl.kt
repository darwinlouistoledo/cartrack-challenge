package com.dlet.cartrack.challenge.data.usecase

import com.dlet.cartrack.challenge.domain.model.Country
import com.dlet.cartrack.challenge.domain.sealedclass.DataResult
import com.dlet.cartrack.challenge.domain.usecase.CountriesUseCase
import com.google.gson.Gson
import io.reactivex.Single
import javax.inject.Inject

class CountriesUseCaseImpl @Inject constructor(
  private val gson: Gson
): CountriesUseCase {
  override fun getAllCountry(rawJson: String): Single<DataResult<List<Country>>> =
    Single.just(rawJson)
      .map {
        gson.fromJson(rawJson, Array<Country>::class.java).toList()
      }
      .map<DataResult<List<Country>>> {
        DataResult.Success(it) }
      .onErrorReturn { DataResult.Failed(it) }
}