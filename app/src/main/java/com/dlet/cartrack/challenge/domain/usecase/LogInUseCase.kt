package com.dlet.cartrack.challenge.domain.usecase

import com.dlet.cartrack.challenge.domain.model.User
import com.dlet.cartrack.challenge.domain.sealedclass.DataResult
import io.reactivex.Single

interface LogInUseCase {
  fun doLogIn(username: String, password: String): Single<DataResult<User>>

  fun createTestUserAccount(user: User): Single<DataResult<User>>
}