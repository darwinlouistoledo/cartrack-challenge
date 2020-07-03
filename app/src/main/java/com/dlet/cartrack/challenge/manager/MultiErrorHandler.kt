package com.dlet.cartrack.challenge.manager

import androidx.fragment.app.Fragment
import com.dlet.cartrack.challenge.domain.manager.ErrorHandler
import timber.log.Timber

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
class MultiErrorHandler : ErrorHandler<Fragment> {

  private val handlers = mutableListOf<ErrorHandler<Fragment>>()

  fun add(handler: ErrorHandler<Fragment>) {
    handlers.add(handler)
  }

  fun clear() {
    handlers.clear()
  }

  override fun canHandle(error: Throwable): Boolean = true

  override fun handle(
    fragment: Fragment,
    error: Throwable
  ) {
    Timber.e(error)
    var isHandled = false
    handlers.forEach {
      if (!isHandled && it.canHandle(error)) {
        isHandled = true
        it.handle(fragment, error)
      }
    }
  }

}