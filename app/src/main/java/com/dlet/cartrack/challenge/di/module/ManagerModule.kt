package com.dlet.cartrack.challenge.di.module

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dlet.cartrack.challenge.data.local.manager.RepositoryCachePrefsImpl
import com.dlet.cartrack.challenge.di.qualifiers.DebugTree
import com.dlet.cartrack.challenge.domain.base.RepositoryCachePrefs
import com.dlet.cartrack.challenge.domain.manager.ErrorHandler
import com.dlet.cartrack.challenge.domain.rx.SchedulerProvider
import com.dlet.cartrack.challenge.manager.ApiErrorHandler
import com.dlet.cartrack.challenge.manager.AppSchedulerProvider
import com.dlet.cartrack.challenge.manager.DefaultErrorHandler
import com.dlet.cartrack.challenge.manager.MultiErrorHandler
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ManagerModule {

  @Provides
  @Singleton
  fun providesSchedulerProvider(): SchedulerProvider =
    AppSchedulerProvider()

  @Singleton
  @Provides
  @DebugTree
  fun appDebugTree(): Timber.Tree = Timber.DebugTree()

  @Singleton
  @Provides
  fun providesRepositoryCachePrefs(
    @ApplicationContext context: Context
  ): RepositoryCachePrefs =
    RepositoryCachePrefsImpl(context)

  @Singleton
  @Provides
  fun errorHandler(gson: Gson): ErrorHandler<AppCompatActivity> {
    val multiErrorHandler =
      MultiErrorHandler()
    multiErrorHandler.add(
        ApiErrorHandler(gson)
    )
    // should be last
    multiErrorHandler.add(
        DefaultErrorHandler(
            multiErrorHandler, "Debug"
        )
    )
    return multiErrorHandler
  }


}