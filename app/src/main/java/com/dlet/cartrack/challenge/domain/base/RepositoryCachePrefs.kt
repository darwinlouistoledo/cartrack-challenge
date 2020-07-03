package com.dlet.cartrack.challenge.domain.base

import com.dlet.cartrack.challenge.domain.enums.CacheStatus


interface RepositoryCachePrefs {

  fun updateCacheUpdate(key: String)

  fun getCacheUpdate(
    key: String,
    staleDate: Long,
    expiryDate: Long? = null
  ): CacheStatus

  fun deleteCachePrefs()

}