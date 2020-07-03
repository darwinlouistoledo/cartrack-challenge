package com.dlet.cartrack.challenge.data.local.dto

import com.dlet.cartrack.challenge.domain.exceptions.QueryCascadeException
import com.dlet.cartrack.challenge.domain.model.User
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.*

@RealmClass
open class UserDto() : RealmObject() {
  @PrimaryKey
  var id: Int = -1
  var name: String = ""
  var username: String = ""
  var email: String = ""
  var password: String? = null
  var phone: String = ""
  var website: String = ""
  var address: AddressDto? = null
  var company: CompanyDto? = null


  constructor(item: User) : this() {
    this.id = item.id
    this.name = item.name
    this.username = item.username
    this.email = item.email
    this.password = item.password
    this.phone = item.phone
    this.website = item.website
    this.address = AddressDto(item.address, item.id, UUID.randomUUID().toString())
    this.company = CompanyDto(item.company, item.id, UUID.randomUUID().toString())
  }

  val toUser: User
    get() = User(
      id = id,
      name = name,
      username = username,
      email = email,
      phone = phone,
      password = password,
      website = website,
      address = address?.toAddress ?: throw QueryCascadeException(),
      company = company?.toCompany ?: throw QueryCascadeException()
    )
}