package com.dlet.cartrack.challenge.testcases

import com.dlet.cartrack.challenge.domain.model.*

object TestData {

  val user1 = User(
    id = 1000,
    name = "Juan Cruz",
    username = "juancruz",
    email = "juancruz@.test.com",
    password = "pass1234",
    phone = "123-4567",
    website = "juancruz.ph",
    address = Address(
      street = null,
      city = null,
      zipCode = null,
      suite = null,
      geoLocation = GeoLocation(0f, 0f)
    ),
    company = Company(
      name = "Juancruz Inc",
      catchPhrase = "Juancruz is awesome",
      bs = "Juancruz-is-awesome"
    )
  )

  val user2 = User(
    id = 1001,
    name = "John Doe",
    username = "johndoe",
    email = "johndoe@.test.com",
    password = "pass4321",
    phone = "123-4567",
    website = "johndoe.com",
    address = Address(
      street = null,
      city = null,
      zipCode = null,
      suite = null,
      geoLocation = GeoLocation(0f, 0f)
    ),
    company = Company(
      name = "Johndoe Inc",
      catchPhrase = "Johndoe is awesome",
      bs = "johndoe-is-awesome"
    )
  )

  val countryOne = Country(
    name = "Philippines",
    code = "PH"
  )

  val countryTwo = Country(
    name = "Afghanistan",
    code = "AF"
  )

  val countryThree = Country(
    name = "Åland Islands",
    code = "AX"
  )

  val countryFour = Country(
    name = "Albania",
    code = "AL"
  )

  val countriesJson = "[{\"name\":\"Afghanistan\",\"code\":\"AF\"},{\"name\":\"Åland Islands\",\"code\":\"AX\"},{\"name\":\"Albania\",\"code\":\"AL\"}]"

  val listOfUsers = listOf(
    user1,
    user2
  )

  val listOfCountries = listOf(
    countryTwo,
    countryThree,
    countryFour
  )

}