package com.dlet.cartrack.challenge.di.module

import android.app.Application
import com.dlet.cartrack.challenge.data.local.dao.UserDao
import com.dlet.cartrack.challenge.data.local.source.UserListLocalSource
import com.dlet.cartrack.challenge.data.local.source.UserLocalSource
import com.dlet.cartrack.challenge.data.local.util.RealmInstance
import com.dlet.cartrack.challenge.data.remote.service.UserApiService
import com.dlet.cartrack.challenge.data.remote.source.UserListRemoteSource
import com.dlet.cartrack.challenge.data.repository.SimpleRepository
import com.dlet.cartrack.challenge.data.usecase.AppInitializationUseCaseImpl
import com.dlet.cartrack.challenge.data.usecase.CountriesUseCaseImpl
import com.dlet.cartrack.challenge.data.usecase.LogInUseCaseImpl
import com.dlet.cartrack.challenge.data.usecase.UserUseCaseImpl
import com.dlet.cartrack.challenge.domain.base.LocalSource
import com.dlet.cartrack.challenge.domain.base.Repository
import com.dlet.cartrack.challenge.domain.base.RepositoryCachePrefs
import com.dlet.cartrack.challenge.domain.keys.UserKey
import com.dlet.cartrack.challenge.domain.model.User
import com.dlet.cartrack.challenge.domain.rx.SchedulerProvider
import com.dlet.cartrack.challenge.domain.sealedclass.Optional
import com.dlet.cartrack.challenge.domain.usecase.AppInitializationUseCase
import com.dlet.cartrack.challenge.domain.usecase.CountriesUseCase
import com.dlet.cartrack.challenge.domain.usecase.LogInUseCase
import com.dlet.cartrack.challenge.domain.usecase.UserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.realm.Realm
import io.realm.RealmConfiguration
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DataModule {

  @Provides
  @Singleton
  fun userApiService(retrofit: Retrofit): UserApiService =
    retrofit.create(UserApiService::class.java)


  @Singleton
  @Provides
  fun realmConfiguration(application: Application): RealmConfiguration {
    Realm.init(application)
    return RealmConfiguration.Builder()
      .deleteRealmIfMigrationNeeded()
      .build()
  }

  @Provides
  fun userListRepository(
    localSource: UserListLocalSource,
    remoteSource: UserListRemoteSource,
    cachePrefs: RepositoryCachePrefs,
    schedulerProvider: SchedulerProvider
  ): Repository<String, List<User>> = SimpleRepository(
    localSource = localSource,
    remoteSource = remoteSource,
    cachePrefs = cachePrefs,
    schedulerProvider = schedulerProvider,
    tag = "userListRepository"
  )

  @Provides
  fun userLocalRepository(
    realmInstance: RealmInstance,
    userDao: UserDao
  ): LocalSource<UserKey, Optional<User>> = UserLocalSource(realmInstance, userDao)

  @Provides
  fun appInitializationUseCase(appInitializationUseCaseImpl: AppInitializationUseCaseImpl): AppInitializationUseCase =
    appInitializationUseCaseImpl

  @Provides
  fun userUseCase(userUseCaseImpl: UserUseCaseImpl): UserUseCase =
    userUseCaseImpl

  @Provides
  fun logInUseCase(logInUseCaseImpl: LogInUseCaseImpl): LogInUseCase =
    logInUseCaseImpl

  @Provides
  fun countriesUseCase(countriesUseCaseImpl: CountriesUseCaseImpl): CountriesUseCase =
    countriesUseCaseImpl
}