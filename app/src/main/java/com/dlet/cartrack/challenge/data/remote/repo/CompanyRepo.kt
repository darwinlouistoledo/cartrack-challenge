package com.dlet.cartrack.challenge.data.remote.repo

import com.dlet.cartrack.challenge.domain.model.Company

data class CompanyRepo(
  val name: String,
  val catchPhrase: String,
  val bs: String
) {
  constructor(item: Company) : this(
    name = item.name,
    catchPhrase = item.catchPhrase,
    bs = item.bs
  )

  val toCompany: Company
    get() = Company(
      name = name,
      catchPhrase = catchPhrase,
      bs = bs
    )
}