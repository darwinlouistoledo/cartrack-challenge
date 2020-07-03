package com.dlet.cartrack.challenge.data.local.source

import com.dlet.cartrack.challenge.data.local.dao.UserDao
import com.dlet.cartrack.challenge.data.local.dto.UserDto
import com.dlet.cartrack.challenge.data.local.util.RealmInstance
import com.dlet.cartrack.challenge.domain.base.LocalSource
import com.dlet.cartrack.challenge.domain.keys.UserKey
import com.dlet.cartrack.challenge.domain.model.User
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class UserLocalSource @Inject constructor(
  private val realmInstance: RealmInstance,
  private val userDao: UserDao
) : LocalSource<UserKey, User> {

  override fun get(key: UserKey): Observable<User> =
    userDao.get(
      realm = realmInstance.getRealm(),
      username = key.username,
      password = key.password
    )
      .map { it.toUser }

  override fun save(
    key: UserKey,
    data: User
  ): Single<User> =
    userDao.save(
      data = UserDto(data)
    )
      .map { it.toUser }
}