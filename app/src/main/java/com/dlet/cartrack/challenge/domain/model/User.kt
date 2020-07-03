package com.dlet.cartrack.challenge.domain.model

data class User(
  val id: Int,
  val name: String,
  val username: String,
  val email: String,
  val password: String?=null,
  val phone: String,
  val website: String,
  val address: Address,
  val company: Company
)