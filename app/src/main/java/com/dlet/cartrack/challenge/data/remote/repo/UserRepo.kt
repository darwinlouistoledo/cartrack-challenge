package com.dlet.cartrack.challenge.data.remote.repo

import com.dlet.cartrack.challenge.domain.model.User

data class UserRepo(
  val id: String,
  val name: String,
  val username: String,
  val email: String,
  val phone: String,
  val website: String,
  val address: AddressRepo,
  val company: CompanyRepo
) {
  constructor(item: User) : this(
    id = item.id,
    name = item.name,
    username = item.username,
    email = item.email,
    phone = item.phone,
    website = item.website,
    address = AddressRepo(item.address),
    company = CompanyRepo(item.company)
  )

  val toUser: User
    get() = User(
      id = id,
      name = name,
      username = username,
      email = email,
      phone = phone,
      website = website,
      address = address.toAddress,
      company = company.toCompany
    )
}