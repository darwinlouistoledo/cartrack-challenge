package com.dlet.cartrack.challenge.domain.exceptions

class InvalidUsernameAndPasswordException() : RuntimeException(
    "Invalid Username and Password."
) {

  override fun equals(other: Any?): Boolean {
    return if (other is InvalidUsernameAndPasswordException){
      this.message == other.message
    } else {
      false
    }
  }
}