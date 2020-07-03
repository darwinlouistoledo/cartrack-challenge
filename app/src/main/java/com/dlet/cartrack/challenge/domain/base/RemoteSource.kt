package com.dlet.cartrack.challenge.domain.base

import io.reactivex.Single

interface RemoteSource<Key, Data> {

  fun fetch(key: Key): Single<Data>

}