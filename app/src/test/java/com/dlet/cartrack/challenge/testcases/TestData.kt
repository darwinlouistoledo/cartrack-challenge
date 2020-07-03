package com.dlet.cartrack.challenge.testcases

import com.dlet.cartrack.challenge.domain.model.Address
import com.dlet.cartrack.challenge.domain.model.Company
import com.dlet.cartrack.challenge.domain.model.GeoLocation
import com.dlet.cartrack.challenge.domain.model.User

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

  val listOfUsers = listOf(
    user1,
    user2
  )

}