package com.dlet.cartrack.challenge.data.local.dao

import com.dlet.cartrack.challenge.data.local.dto.UserDto
import com.dlet.cartrack.challenge.data.local.util.RealmHelper
import com.dlet.cartrack.challenge.data.local.util.findAllObservable
import com.dlet.cartrack.challenge.data.local.util.saveToRealm
import com.dlet.cartrack.challenge.domain.sealedclass.Optional
import com.dlet.cartrack.challenge.domain.sealedclass.toOptional
import io.reactivex.Observable
import io.reactivex.Single
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.where
import javax.inject.Inject

class UserDao @Inject constructor() {

  fun get(
    realm: Realm,
    username: String,
    password: String
  ): Observable<Optional<UserDto>> = RealmHelper.rxTransaction { realm ->
    realm.where<UserDto>()
      .equalTo(UserDto::username.name, username)
      .equalTo(UserDto::password.name, password)
      .findFirst()
      .toOptional()
  }
    .toObservable()

  fun getAll(realm: Realm, key: String): Observable<List<UserDto>> =
    realm.where<UserDto>()
      .sort(UserDto::name.name, Sort.ASCENDING)
      .findAllObservable()


  fun saveAll(
    data: List<UserDto>
  ): Single<List<UserDto>> = RealmHelper.rxTransaction { realm ->
    realm.where<UserDto>()
      .findAll()
      .deleteAllFromRealm()
    realm.saveToRealm(data)
  }

  fun save(
    data: UserDto
  ): Single<UserDto> = RealmHelper.rxTransaction { realm ->
    val oldData: UserDto? = realm.where<UserDto>()
      .equalTo(UserDto::id.name, data.id)
      .findFirst()

    oldData?.deleteFromRealm()

    realm.saveToRealm(data)
  }

}