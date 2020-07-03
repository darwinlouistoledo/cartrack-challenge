package com.dlet.cartrack.challenge.data.repository

import com.dlet.cartrack.challenge.domain.base.LocalSource
import com.dlet.cartrack.challenge.domain.base.RemoteSource
import com.dlet.cartrack.challenge.domain.base.Repository
import com.dlet.cartrack.challenge.domain.base.RepositoryCachePrefs
import com.dlet.cartrack.challenge.domain.enums.CacheStatus
import com.dlet.cartrack.challenge.domain.rx.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.Single

/**
 * This a generic class that will be managing the retrieval of data. based on key and data type.
 *
 * @param Key The key for data retrieval
 * @param Data The data type of the result
 */
open class SimpleRepository<Key, Data>(
  private val localSource: LocalSource<Key, Data>,
  private val remoteSource: RemoteSource<Key, Data>,
  private val cachePrefs: RepositoryCachePrefs,
  private val schedulerProvider: SchedulerProvider,
  private val staleDate: Long = 60000*5,
  private val expiryDate: Long? = 60000*60,
  private val tag: String = this::class.java.simpleName
) : Repository<Key, Data>() {

  override fun getCacheStatus(key: Key): CacheStatus = cachePrefs.getCacheUpdate(
      "$tag-$key",
      staleDate,
      expiryDate
  )

  override fun updateCacheStatus(key: Key) {
    cachePrefs.updateCacheUpdate("$tag-$key")
  }

  override fun query(key: Key): Observable<Data> = Observable
      .defer { localSource.get(key) }
      .subscribeOn(schedulerProvider.ui())

  override fun fetch(key: Key): Single<Data> = remoteSource.fetch(key)
      .subscribeOn(schedulerProvider.io())

  override fun save(
    key: Key,
    data: Data
  ): Single<Data> = localSource.save(key, data)
      .subscribeOn(schedulerProvider.io())

}