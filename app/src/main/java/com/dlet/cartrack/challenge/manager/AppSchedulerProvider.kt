package com.dlet.cartrack.challenge.manager

import com.dlet.cartrack.challenge.domain.rx.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class AppSchedulerProvider : SchedulerProvider {

  init {
    Timber.d("SchedulerProvider: AppSchedulerProvider")
  }

  //This scheduler is used for processing in other threads
  override fun io(): Scheduler = Schedulers.io()

  //This scheduler is used for processing in Main UI thread
  override fun ui(): Scheduler = AndroidSchedulers.mainThread()

  //This scheduler is used for processing computation
  override fun computation(): Scheduler = Schedulers.computation()
}