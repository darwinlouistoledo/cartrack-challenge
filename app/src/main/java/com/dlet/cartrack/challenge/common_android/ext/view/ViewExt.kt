package com.dlet.cartrack.challenge.common_android.ext.view

import android.view.View

/**
 * Makes the view visible
 */
fun View.makeVisible() {
  visibility = View.VISIBLE
}

/**
 * Makes the view gone
 */
fun View.makeGone() {
  visibility = View.GONE
}

/**
 * Makes this view visible if condition is true, gone otherwise.
 */
fun View.makeVisibleOrGone(condition: Boolean) {
  when (condition) {
    true -> this.makeVisible()
    else -> this.makeGone()
  }
}