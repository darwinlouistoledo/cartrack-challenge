package com.dlet.cartrack.challenge.data.local.dto

import com.dlet.cartrack.challenge.domain.exceptions.QueryCascadeException
import com.dlet.cartrack.challenge.domain.model.Address
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.*

@RealmClass
open class AddressDto() : RealmObject() {
  @PrimaryKey
  var id: String? = ""
  var userId: String = ""
  var street: String? = null
  var suite: String? = null
  var city: String? = null
  var zipCode: String? = null
  var geoLocation: GeoLocationDto? = null

  constructor(item: Address, userId: String, generatedId: String) : this() {
    this.id = generatedId
    this.userId = userId
    this.street = item.street
    this.suite = item.suite
    this.city = item.city
    this.zipCode = item.zipCode
    this.geoLocation = GeoLocationDto(item.geoLocation, userId, UUID.randomUUID().toString())
  }

  val toAddress: Address
    get() = Address(
      street = street,
      suite = suite,
      city = city,
      zipCode = zipCode,
      geoLocation = geoLocation?.toGeoLocation?: throw QueryCascadeException()
    )
}
