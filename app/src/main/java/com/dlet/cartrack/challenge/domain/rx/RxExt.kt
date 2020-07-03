package com.dlet.cartrack.challenge.domain.rx

import io.reactivex.ObservableEmitter
import io.reactivex.SingleEmitter

fun <T> SingleEmitter<T>.success(value: T) {
  if (!isDisposed) {
    onSuccess(value)
  }
}

fun <T> SingleEmitter<T>.error(error: Throwable) {
  if (!isDisposed) {
    onError(error)
  }
}

fun <T> ObservableEmitter<T>.next(value: T) {
  if (!isDisposed) {
    onNext(value)
  }
}

fun <T> ObservableEmitter<T>.error(error: Throwable) {
  if (!isDisposed) {
    onError(error)
  }
}

fun <T> ObservableEmitter<T>.complete() {
  if (!isDisposed) {
    onComplete()
  }
}