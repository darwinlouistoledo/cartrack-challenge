package com.dlet.cartrack.challenge.data.usecase

import com.dlet.cartrack.challenge.domain.base.Repository
import com.dlet.cartrack.challenge.domain.model.User
import com.dlet.cartrack.challenge.domain.sealedclass.DataResult
import com.dlet.cartrack.challenge.domain.usecase.UserUseCase
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

class UserUseCaseImpl @Inject constructor(
  private val userListRepository: Repository<String, List<User>>
) : UserUseCase {

  override fun getUsers(): Observable<DataResult<List<User>>> =
    userListRepository.get("all")
      .map<DataResult<List<User>>> {
        Timber.i("All Users: ${it.map { it.name }}")
        DataResult.Success(it)
      }
      .onErrorReturn { DataResult.Failed(it) }

}