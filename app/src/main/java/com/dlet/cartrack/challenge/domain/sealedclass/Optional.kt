package com.dlet.cartrack.challenge.domain.sealedclass

sealed class Optional<out T> {

  abstract fun getValue(): T

  fun getValueOrNull(): T? = try {
    getValue()
  } catch (e: NoSuchElementException) {
    null
  }

  class Some<out T>(val element: T) : Optional<T>() {
    override fun getValue(): T = element
  }

  object None : Optional<Nothing>() {
    override fun getValue(): Nothing = throw NoSuchElementException()
  }

}

fun <T> T?.toOptional(): Optional<T> = when (this) {
  null -> Optional.None
  else -> Optional.Some(this)
}