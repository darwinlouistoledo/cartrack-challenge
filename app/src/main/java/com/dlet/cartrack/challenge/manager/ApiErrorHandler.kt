package com.dlet.cartrack.challenge.manager

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dlet.cartrack.challenge.common_android.ext.component.showToast
import com.dlet.cartrack.challenge.domain.manager.ErrorHandler
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import java.io.IOException

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
class ApiErrorHandler(private val gson: Gson) : ErrorHandler<AppCompatActivity> {

  override fun canHandle(error: Throwable): Boolean =
    error is HttpException && error.code() in 400..499 && error.code() != 401

  override fun handle(
    activity: AppCompatActivity,
    error: Throwable
  ) {
    val httpException = error as HttpException
    val response = httpException.response()
    try {
      activity.applicationContext
          .showToast("An API Error has occurred")
    } catch (e: IOException) {
      e.printStackTrace()
    } catch (e: JsonSyntaxException) {
      e.printStackTrace()
    }
  }

}