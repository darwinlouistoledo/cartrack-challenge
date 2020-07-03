package com.dlet.cartrack.challenge.data.local.manager

import android.content.Context
import com.dlet.cartrack.challenge.domain.base.RepositoryCachePrefs
import com.dlet.cartrack.challenge.domain.base.PreferenceHelper.customPrefs
import com.dlet.cartrack.challenge.domain.base.PreferenceHelper.set
import com.dlet.cartrack.challenge.domain.base.PreferenceHelper.get
import com.dlet.cartrack.challenge.domain.enums.CacheStatus
import timber.log.Timber
import java.util.Date

class RepositoryCachePrefsImpl(private val context: Context) : RepositoryCachePrefs {

  companion object {
    private const val CACHE_PREFS = "cache_prefs"
  }

  override fun updateCacheUpdate(key: String) {
    customPrefs(context,
        CACHE_PREFS
    )[key] = Date().time
  }

  override fun getCacheUpdate(
    key: String,
    staleDate: Long,
    expiryDate: Long?
  ): CacheStatus {
    val time: Long? = customPrefs(
        context,
        CACHE_PREFS
    )[key]
    val lastUpdate = time?.let {
      val date = Date()
      date.time = it
      date
    }
    val cacheStatus = when {
      (lastUpdate == null && expiryDate != null) || (lastUpdate != null && isElapsed(
          expiryDate,
          lastUpdate
      )) -> CacheStatus.EXPIRED
      (lastUpdate == null && expiryDate == null) || (lastUpdate != null && isElapsed(
          staleDate,
          lastUpdate
      )) -> CacheStatus.STALE
      else -> CacheStatus.FRESH
    }
    Timber.d("getCacheUpdate: key=$key, cacheStatus=${cacheStatus.name}")
    return cacheStatus
  }

  override fun deleteCachePrefs() = customPrefs(context,
      CACHE_PREFS
  )
      .edit()
      .clear()
      .apply()

  private fun isElapsed(
    date: Long?,
    lastUpdate: Date
  ) = date != null && Date().time - lastUpdate.time >= date

}