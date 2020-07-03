package com.dlet.cartrack.challenge

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.dlet.cartrack.challenge.di.qualifiers.DebugTree
import com.dlet.cartrack.challenge.domain.usecase.AppInitializationUseCase
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application() {

  @Inject
  lateinit var appInitializationUseCase: AppInitializationUseCase

  @Inject
  lateinit var lifecycleListener: AppLifecycleListener

  @field:[Inject DebugTree]
  lateinit var debugTree: Timber.Tree

  override fun onCreate() {
    super.onCreate()

    Timber.plant(debugTree)

    appInitializationUseCase.init()

    ProcessLifecycleOwner.get()
        .lifecycle.addObserver(lifecycleListener)

  }

}