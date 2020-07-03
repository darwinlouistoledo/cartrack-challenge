package com.dlet.cartrack.challenge.data.local.util

import com.dlet.cartrack.challenge.domain.exceptions.NoRecordsFoundException
import com.dlet.cartrack.challenge.domain.rx.error
import com.dlet.cartrack.challenge.domain.rx.success
import io.reactivex.Observable
import io.reactivex.Single
import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmQuery
import timber.log.Timber

object RealmHelper {

  fun <T : Any> rxTransaction(transaction: (Realm) -> T): Single<T> = Single.create<T> { emitter ->
    try {
      Realm.getDefaultInstance()
          .use { realm ->
            var result: T? = null
            Timber.d("rxTransaction thread: ${Thread.currentThread().name}")
            realm.executeTransaction {
              result = transaction.invoke(it)
            }
            emitter.success(result)
          }
    } catch (e: Throwable) {
      Timber.e(e)
      emitter.error(e)
    }
  }

}

inline fun <reified T : RealmObject> Realm.saveToRealm(items: List<T>): List<T> =
  this.copyFromRealm(this.copyToRealmOrUpdate(items))

inline fun <reified T : RealmObject> Realm.saveToRealm(item: T): T =
  this.copyFromRealm(this.copyToRealmOrUpdate(item))

inline fun <reified T : RealmObject> RealmQuery<T>.findAllObservable(): Observable<List<T>> =
  when {
    realm.isAutoRefresh -> findAllAsync()
        .asFlowable()
        .doOnNext { Timber.d("findAllObservable thread: ${Thread.currentThread().name}") }
        .filter { it.isLoaded && it.isValid }
        .map {
          it.freeze()
              .toList()
        }
        .toObservable()
    else -> Observable.just(
            findAll().freeze()
                .toList()
        )
        .doOnNext {
          Timber.d("findAllObservable non auto-refresh thread: ${Thread.currentThread().name}")
        }
  }

inline fun <reified T : RealmObject> RealmQuery<T>.findObservable(): Observable<T> = when {
  realm.isAutoRefresh -> findAllAsync()
      .asFlowable()
      .doOnNext { Timber.d("findAllObservable thread: ${Thread.currentThread().name}") }
      .filter { it.isLoaded && it.isValid }
      .map {
        it.freeze()
            .firstOrNull() ?: throw NoRecordsFoundException("No object found")
      }
      .toObservable()
  else -> Observable.just(
          findAll().let {
            it.freeze()
                .firstOrNull() ?: throw NoRecordsFoundException("No object found")
          })
      .doOnNext {
        Timber.d("findAllObservable non auto-refresh thread: ${Thread.currentThread().name}")
      }
}
