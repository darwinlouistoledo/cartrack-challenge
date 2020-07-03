package com.dlet.cartrack.challenge

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.dlet.cartrack.challenge.domain.usecase.AppInitializationUseCase
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

class AppLifecycleListener @Inject constructor(
  private val appInitializationUseCase: AppInitializationUseCase
) : LifecycleObserver {

  private val disposeBag = CompositeDisposable()

  /**
   * Lifecycle callback when app was set to foreground
   */
  @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
  fun onAppCreated() {
    Timber.d("onAppCreated() -> Lifecycle.Event.ON_CREATE")

  }

  @OnLifecycleEvent(Lifecycle.Event.ON_START)
  fun onAppStart() {
    Timber.d("onAppStart() -> Lifecycle.Event.ON_START")
    appInitializationUseCase.onAppCreated()
  }

  /**
   * Lifecycle callback when app was set to foreground
   */
  @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
  fun onAppResumed() {
    Timber.d("onAppResumed() -> Lifecycle.Event.ON_RESUME")

  }

  /**
   * Lifecycle callback when app was set to background
   */
  @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
  fun onAppPaused() {
    Timber.d("onAppPaused() -> Lifecycle.Event.ON_PAUSE")

  }

  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  fun onAppStop() {
    Timber.d("onAppStop() -> Lifecycle.Event.ON_STOP")
    appInitializationUseCase.onAppDestroyed()
    disposeBag.clear()
  }

}