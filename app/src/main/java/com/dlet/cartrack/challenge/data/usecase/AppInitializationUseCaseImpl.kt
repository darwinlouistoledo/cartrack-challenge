package com.dlet.cartrack.challenge.data.usecase

import com.dlet.cartrack.challenge.data.local.util.RealmInstance
import com.dlet.cartrack.challenge.domain.rx.SchedulerProvider
import com.dlet.cartrack.challenge.domain.usecase.AppInitializationUseCase
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppInitializationUseCaseImpl @Inject constructor(
  private val realmConfiguration: RealmConfiguration,
  private val realmInstance: RealmInstance,
  private val schedulerProvider: SchedulerProvider
): AppInitializationUseCase{

  private val disposeBag = CompositeDisposable()

  override fun init() {
    Realm.setDefaultConfiguration(realmConfiguration)
  }

  override fun onAppCreated() {
    Observable
        .defer {
          realmInstance.openRealm()
          Observable.just(true)
        }
        .subscribeOn(schedulerProvider.ui())
        .subscribe()
        .addTo(disposeBag)
  }

  override fun onAppDestroyed() {
    Observable
        .defer {
          realmInstance.close()
          Observable.just(true)
        }
        .subscribeOn(schedulerProvider.ui())
        .subscribe()
        .addTo(disposeBag)

    disposeBag.clear()
  }
}