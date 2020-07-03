package com.dlet.cartrack.challenge.domain.base

import com.dlet.cartrack.challenge.domain.enums.CacheStatus
import com.dlet.cartrack.challenge.domain.enums.CacheStatus.EXPIRED
import com.dlet.cartrack.challenge.domain.enums.CacheStatus.FRESH
import com.dlet.cartrack.challenge.domain.enums.CacheStatus.STALE
import com.dlet.cartrack.challenge.domain.exceptions.NoRecordsFoundException
import com.dlet.cartrack.challenge.domain.exceptions.QueryCascadeException
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.functions.Function

/**
 * if fresh -> emits local source
 * if stale -> emit local source first but also refresh
 * if expired -> refresh then get local source
 */
abstract class Repository<Key, Data> {

  companion object {
    const val IGNORE_KEY = 0L
  }

  private val isInitialDataLoaded = mutableMapOf<Key, Boolean>()

  abstract fun getCacheStatus(key: Key): CacheStatus

  abstract fun updateCacheStatus(key: Key)

  abstract fun query(key: Key): Observable<Data>

  abstract fun fetch(key: Key): Single<Data>

  abstract fun save(
    key: Key,
    data: Data
  ): Single<Data>

  /**
   * query from local database,
   */
  fun get(key: Key): Observable<Data> = when (getCacheStatus(key)) {
    FRESH -> getLocal(key)
        .switchMap { forceRefreshIfEmptyObservable(key, it) }
    STALE -> query(key)
        .take(1)
        .onErrorResumeNext(errorResumeFunction(key))
        .switchMap { data ->
          refresh(key)
              .toObservable()
              .switchMap { getLocal(key) }
              .startWithDataIfNotEmpty(data)
        }
    EXPIRED -> refresh(key)
        .toObservable()
        .switchMap { getLocal(key) }
  }.doOnSubscribe { isInitialDataLoaded[key] = true }

  /**
   * Trigger fetch data from network and save result
   */
  fun refresh(key: Key): Single<Data> = fetch(key)
      .flatMap { save(key, it) }
      .doFinally {
        updateCacheStatus(key)
        isInitialDataLoaded[key] = false
      }

  private fun getLocal(key: Key): Observable<Data> = query(key)
      .onErrorResumeNext(errorResumeFunction(key))

  private fun errorResumeFunction(key: Key): Function<Throwable, ObservableSource<out Data>> =
    Function { error ->
      when {
        (error is NoRecordsFoundException && isInitialDataLoaded[key] ?: true)
            || error is QueryCascadeException -> refresh(key)
            .toObservable()
            .flatMap { getLocal(key) }
        else -> throw error
      }
    }

  private fun forceRefreshIfEmptyObservable(
    key: Key,
    data: Data
  ): Observable<Data> = when {
    isInitialDataLoaded[key] ?: true &&
        (data as? Collection<*>)?.isEmpty() == true -> refresh(key)
        .toObservable()
        .flatMap { getLocal(key) }
    else -> Observable.just(data)
  }

  private fun Observable<Data>.startWithDataIfNotEmpty(data: Data): Observable<Data> = when {
    (data as? Collection<*>)?.isEmpty() == true -> this
    else -> this.startWith(data)
  }

}