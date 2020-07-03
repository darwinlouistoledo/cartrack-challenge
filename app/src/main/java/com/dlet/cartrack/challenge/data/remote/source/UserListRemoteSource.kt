package com.dlet.cartrack.challenge.data.remote.source

import com.dlet.cartrack.challenge.data.remote.service.UserApiService
import com.dlet.cartrack.challenge.domain.base.RemoteSource
import com.dlet.cartrack.challenge.domain.model.User
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

class UserListRemoteSource @Inject constructor(
  private val userApiService: UserApiService
) : RemoteSource<String, List<User>> {

  override fun fetch(key: String): Single<List<User>> =
    userApiService.getUserList()
      .map {
        Timber.i("fetch: All Users: ${it.map { it.name }}")

        it.map { repo -> repo.toUser }
      }
}