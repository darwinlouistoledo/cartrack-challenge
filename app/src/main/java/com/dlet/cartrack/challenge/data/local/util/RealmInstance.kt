package com.dlet.cartrack.challenge.data.local.util

import io.realm.Realm
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealmInstance @Inject constructor() {

  private val realms: ThreadLocal<Realm> = ThreadLocal()

  fun getRealm(): Realm {
    Timber.d("RealmInstance: getRealm thread: ${Thread.currentThread().name}")
    return realms.get() ?: openRealm()
  }

  fun openRealm(): Realm {
    var realm = realms.get()
    if (realm != null && !realm.isClosed) {
      return realm
    }
    Timber.d("RealmInstance: openRealm thread: ${Thread.currentThread().name}")
    realm = Realm.getDefaultInstance()
    realms.set(realm)
    return realm!!
  }

  fun close() {
    if (realms.get()?.isClosed == false) {
      Timber.d("RealmInstance: close thread: ${Thread.currentThread().name}")
      realms.get()
          ?.close()
    }
    realms.set(null)
  }

}