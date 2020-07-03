package com.dlet.cartrack.challenge.testcases

import com.dlet.cartrack.challenge.domain.sealedclass.DataResult
import com.dlet.cartrack.challenge.domain.usecase.CountriesUseCase
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Single
import org.amshove.kluent.mock
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CountriesUseCaseTest {

  @Test
  fun getAllCountriesTest(){
    //GIVEN
    val mockCountriesUseCase = mock<CountriesUseCase>()

    doReturn(Single.just(DataResult.Success(TestData.listOfCountries)))
      .`when`(mockCountriesUseCase)
      .getAllCountry(TestData.countriesJson)

    //WHEN
    val result = mockCountriesUseCase.getAllCountry(TestData.countriesJson).test()

    //THEN
    verify(mockCountriesUseCase).getAllCountry(TestData.countriesJson)

    result.assertNoErrors()
    result.assertComplete()
    result.assertValueCount(1)
    result.assertValue(DataResult.Success(TestData.listOfCountries))
  }

  @Test
  fun getCountryOfLocaleTest(){
    //GIVEN
    val mockCountriesUseCase = mock<CountriesUseCase>()

    doReturn(Single.just(DataResult.Success(TestData.countryFour)))
      .`when`(mockCountriesUseCase)
      .getCountryOfLocale(TestData.countriesJson, TestData.countryFour.code)

    //WHEN
    val result = mockCountriesUseCase.getCountryOfLocale(TestData.countriesJson, TestData.countryFour.code).test()

    //THEN
    verify(mockCountriesUseCase).getCountryOfLocale(TestData.countriesJson, TestData.countryFour.code)

    result.assertNoErrors()
    result.assertComplete()
    result.assertValueCount(1)
    result.assertValue(DataResult.Success(TestData.countryFour))
  }


}