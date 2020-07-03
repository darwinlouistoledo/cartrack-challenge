package com.dlet.cartrack.challenge.manager

import androidx.appcompat.app.AppCompatActivity
import com.dlet.cartrack.challenge.domain.manager.ErrorHandler
import timber.log.Timber

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
class MultiErrorHandler : ErrorHandler<AppCompatActivity> {

  private val handlers = mutableListOf<ErrorHandler<AppCompatActivity>>()

  fun add(handler: ErrorHandler<AppCompatActivity>) {
    handlers.add(handler)
  }

  fun clear() {
    handlers.clear()
  }

  override fun canHandle(error: Throwable): Boolean = true

  override fun handle(
    activity: AppCompatActivity,
    error: Throwable
  ) {
    Timber.e(error)
    var isHandled = false
    handlers.forEach {
      if (!isHandled && it.canHandle(error)) {
        isHandled = true
        it.handle(activity, error)
      }
    }
  }

}