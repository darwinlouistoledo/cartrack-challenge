package com.dlet.cartrack.challenge.domain.exceptions

class RequiredFieldException(fieldName: String) : RuntimeException(
    "Field $fieldName is required."
) {

  override fun equals(other: Any?): Boolean {
    return if (other is RequiredFieldException){
      this.message == other.message
    } else {
      false
    }
  }
}