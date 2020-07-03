package com.dlet.cartrack.challenge.data.remote.repo

import com.dlet.cartrack.challenge.domain.model.GeoLocation
import com.google.gson.annotations.SerializedName

data class GeoLocationRepo(
  @SerializedName("lat") val latitude: Float,
  @SerializedName("lng") val longitude: Float
){
  constructor(item: GeoLocation): this(
    latitude = item.latitude,
    longitude = item.longitude
  )

  val toGeoLocation: GeoLocation
    get() = GeoLocation(
      latitude = latitude,
      longitude = longitude
    )
}