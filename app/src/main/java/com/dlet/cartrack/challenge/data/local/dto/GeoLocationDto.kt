package com.dlet.cartrack.challenge.data.local.dto

import com.dlet.cartrack.challenge.domain.model.GeoLocation
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class GeoLocationDto() : RealmObject(){
  @PrimaryKey
  var id: String = ""
  var userId: Int = -1
  var latitude: Float ?= 0f
  var longitude: Float ?= 0f

  constructor(item: GeoLocation, userId: Int, generatedId: String): this(){
    this.id = generatedId
    this.userId = userId
    this.latitude = item.latitude
    this.longitude = item.longitude
  }

  val toGeoLocation: GeoLocation
    get() = GeoLocation(
      latitude = latitude?:0f,
      longitude = longitude?:0f
    )
}