package com.dlet.cartrack.challenge.data.local.source

import com.dlet.cartrack.challenge.data.local.dao.UserDao
import com.dlet.cartrack.challenge.data.local.dto.UserDto
import com.dlet.cartrack.challenge.data.local.util.RealmInstance
import com.dlet.cartrack.challenge.domain.base.LocalSource
import com.dlet.cartrack.challenge.domain.model.User
import io.reactivex.Observable
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

class UserListLocalSource @Inject constructor(
  private val realmInstance: RealmInstance,
  private val userDao: UserDao
) : LocalSource<String, List<User>> {

  override fun get(key: String): Observable<List<User>> =
    userDao.getAll(
      realm = realmInstance.getRealm(),
      key = key
    )
      .map {
        Timber.i("get: All Users: ${it.map { it.name }}")
        it.map { dto -> dto.toUser }
      }

  override fun save(
    key: String,
    data: List<User>
  ): Single<List<User>> =
    userDao.saveAll(
      data = data.map { UserDto(it) }
    )
      .map {
        Timber.i("saveAll: All Users: ${it.map { it.name }}")
        it.map { dto -> dto.toUser }
      }
}