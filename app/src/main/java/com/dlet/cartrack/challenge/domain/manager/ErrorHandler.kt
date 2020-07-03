package com.dlet.cartrack.challenge.domain.manager

interface ErrorHandler<T> {

  fun canHandle(error: Throwable): Boolean

  fun handle(t: T, error: Throwable)

}