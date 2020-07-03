package com.dlet.cartrack.challenge.common_android.ext.component

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

fun Context.showToast(message: String) {
  Toast.makeText(this, message, Toast.LENGTH_LONG)
      .show()
}

fun Context.showToast(@StringRes message: Int) {
  Toast.makeText(this, message, Toast.LENGTH_LONG)
      .show()
}

fun Context.getDrawable(drawableResName: String): Drawable {
  val resourceId = resources.getIdentifier(drawableResName, "drawable", packageName)
  return ContextCompat.getDrawable(this, resourceId) ?: resources.getDrawable(resourceId, null)
}