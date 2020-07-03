package com.dlet.cartrack.challenge.common_android.img

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

@GlideModule
class CustomGlideModule : AppGlideModule() {

  override fun applyOptions(
    context: Context,
    builder: GlideBuilder
  ) {
    super.applyOptions(context, builder)
    val diskCacheSizeBytes: Long = 1024 * 1024 * 50 //50Mb

    val calculator = MemorySizeCalculator.Builder(context)
        .setMemoryCacheScreens(2f)
        .setBitmapPoolScreens(3f)
        .build()

    builder.setDefaultRequestOptions(
            RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .downsample(DownsampleStrategy.AT_LEAST)
        )
        .setDefaultTransitionOptions(
            Drawable::class.java, DrawableTransitionOptions.withCrossFade()
        )
        .setMemoryCache(LruResourceCache(calculator.memoryCacheSize.toLong()))
        .setBitmapPool(LruBitmapPool(calculator.bitmapPoolSize.toLong()))
        .setDiskCache(
            InternalCacheDiskCacheFactory(context, "cachedImagesFolder", diskCacheSizeBytes)
        )
        .setLogLevel(Log.DEBUG)
  }

  override fun isManifestParsingEnabled(): Boolean {
    return false
  }
}