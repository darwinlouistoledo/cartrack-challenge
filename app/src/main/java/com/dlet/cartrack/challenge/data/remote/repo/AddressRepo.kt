package com.dlet.cartrack.challenge.data.remote.repo

import com.dlet.cartrack.challenge.domain.model.Address
import com.google.gson.annotations.SerializedName

data class AddressRepo(
  val street: String?,
  val suite: String?,
  val city: String?,
  @SerializedName("zipcode") val zipCode: String?,
  @SerializedName("geo") val geoLocation: GeoLocationRepo
){
  constructor(item: Address): this(
    street = item.street,
    suite = item.suite,
    city = item.city,
    zipCode = item.zipCode,
    geoLocation = GeoLocationRepo(item.geoLocation)
  )

  val toAddress: Address
    get() = Address(
      street = street,
      suite = suite,
      city = city,
      zipCode = zipCode,
      geoLocation = geoLocation.toGeoLocation
    )

}
