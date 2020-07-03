package com.dlet.cartrack.challenge.domain.model

import java.lang.StringBuilder

data class Address(
  val street: String?,
  val suite: String?,
  val city: String?,
  val zipCode: String?,
  val geoLocation: GeoLocation
) {
  val displayAddress = StringBuilder()
    .apply {
      street?.let {
        append("$it, ")
      }
      suite?.let {
        append("$it, ")
      }
      city?.let {
        append("$it, ")
      }
      zipCode?.let {
        append("$it")
      }
    }
}
