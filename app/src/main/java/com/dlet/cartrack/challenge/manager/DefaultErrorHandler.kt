package com.dlet.cartrack.challenge.manager

import androidx.fragment.app.Fragment
import com.dlet.cartrack.challenge.common_android.ext.component.showToast
import com.dlet.cartrack.challenge.domain.exceptions.DefaultUserReadableException
import com.dlet.cartrack.challenge.domain.manager.ErrorHandler
import io.reactivex.exceptions.CompositeException

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
class DefaultErrorHandler(
  private val compositeErrorHandler: ErrorHandler<Fragment>,
  private val buildType: String
) : ErrorHandler<Fragment> {

  private val defaultMessage = "Something went wrong. Please try again…"

  override fun canHandle(error: Throwable): Boolean = true

  override fun handle(
    fragment: Fragment,
    error: Throwable
  ) = when (error) {
    is CompositeException -> error.exceptions.forEach {
      compositeErrorHandler.handle(fragment, it)
    }
    is DefaultUserReadableException -> fragment.requireContext()
        .showToast(error.message ?: defaultMessage)
    else -> fragment.requireContext()
      .showToast(error.message ?: defaultMessage)
  }

}