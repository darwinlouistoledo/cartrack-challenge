package com.dlet.cartrack.challenge.data.usecase

import com.dlet.cartrack.challenge.data.local.source.UserLocalSource
import com.dlet.cartrack.challenge.domain.exceptions.InvalidUsernameAndPasswordException
import com.dlet.cartrack.challenge.domain.exceptions.RequiredFieldException
import com.dlet.cartrack.challenge.domain.keys.UserKey
import com.dlet.cartrack.challenge.domain.model.User
import com.dlet.cartrack.challenge.domain.sealedclass.DataResult
import com.dlet.cartrack.challenge.domain.sealedclass.toOptional
import com.dlet.cartrack.challenge.domain.usecase.LogInUseCase
import io.reactivex.Single
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LogInUseCaseImpl @Inject constructor(
  private val userLocalSource: UserLocalSource
) : LogInUseCase {

  override fun doLogIn(username: String, password: String): Single<DataResult<User>> =
    when {
      username.isEmpty() -> Single.just(DataResult.Failed(RequiredFieldException("Username")))
      password.isEmpty() -> Single.just(DataResult.Failed(RequiredFieldException("Password")))
      else -> userLocalSource.get(UserKey(username = username, password = password))
        .delay(2, TimeUnit.SECONDS)
        .singleOrError()
        .map {
          Timber.i("Login User -> $it")
          it.getValueOrNull()?.let { u ->
            DataResult.Success(u)
          } ?: DataResult.Failed(InvalidUsernameAndPasswordException())

        }
        .onErrorReturn { DataResult.Failed(InvalidUsernameAndPasswordException()) }
    }

  override fun createTestUserAccount(user: User): Single<DataResult<User>> =
    userLocalSource.save(UserKey(username = user.username, password = user.password!!), user.toOptional())
      .map<DataResult<User>> {
        DataResult.Success(it.getValue())
      }
      .onErrorReturn { DataResult.Failed(it) }

}