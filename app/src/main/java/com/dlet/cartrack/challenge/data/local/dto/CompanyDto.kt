package com.dlet.cartrack.challenge.data.local.dto

import com.dlet.cartrack.challenge.domain.model.Company
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class CompanyDto() : RealmObject() {
  @PrimaryKey
  var id: String = ""
  var userId: Int = -1
  var name: String = ""
  var catchPhrase: String = ""
  var bs: String = ""

  constructor(item: Company, userId: Int, generatedId: String) : this() {
    this.id = generatedId
    this.userId = userId
    this.name = item.name
    this.catchPhrase = item.catchPhrase
    this.bs = item.bs
  }

  val toCompany: Company
    get() = Company(
      name = name,
      catchPhrase = catchPhrase,
      bs = bs
    )
}