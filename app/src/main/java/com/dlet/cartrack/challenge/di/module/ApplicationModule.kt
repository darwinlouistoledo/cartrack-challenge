package com.dlet.cartrack.challenge.di.module

import android.app.Application
import com.dlet.cartrack.challenge.BuildConfig
import com.dlet.cartrack.challenge.di.qualifiers.ApiEndpoint
import com.dlet.cartrack.challenge.di.qualifiers.AppHttpLoggingInterceptor
import com.dlet.cartrack.challenge.di.qualifiers.IsDebug
import com.dlet.cartrack.challenge.domain.rx.SchedulerProvider
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ApplicationModule {

  @Provides
  @Singleton
  @ApiEndpoint
  fun provideApiEndpoint(): String = BuildConfig.WEB_SERVICES_DOMAIN

  @Provides
  @Singleton
  @IsDebug
  fun provideIsDebug(): Boolean = true

  @Provides
  @Singleton
  fun provideGson(): Gson = GsonBuilder()
      .setPrettyPrinting()
      .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
      .create()

  @Provides
  @Singleton
  @AppHttpLoggingInterceptor
  fun providesHttpLoggingInterceptor(): Interceptor =
    HttpLoggingInterceptor().apply {
      level = HttpLoggingInterceptor.Level.BODY
    }

  @Provides
  @Singleton
  fun provideHttpCache(application: Application): Cache {
    val cacheSize = 10 * 1024 * 1024
    return Cache(application.cacheDir, cacheSize.toLong())
  }

  @Provides
  @Singleton
  fun provideOkhttpClient(
    cache: Cache,
    @AppHttpLoggingInterceptor httpLoggingInterceptor: Interceptor,
    @IsDebug debugMode: Boolean
  ): OkHttpClient = OkHttpClient.Builder()
      .cache(cache)
      .apply {
        if (debugMode) {
          addNetworkInterceptor(httpLoggingInterceptor)
        }
      }
      .readTimeout(60, TimeUnit.SECONDS)
      .connectTimeout(60, TimeUnit.SECONDS)
      .writeTimeout(60, TimeUnit.SECONDS)
      .retryOnConnectionFailure(true)
      .build()

  @Provides
  @Singleton
  fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory =
    GsonConverterFactory.create(gson)

  @Provides
  @Singleton
  fun provideRxJava2CallAdapterFactory(
    schedulerProvider: SchedulerProvider
  ): RxJava2CallAdapterFactory =
    RxJava2CallAdapterFactory.createWithScheduler(schedulerProvider.io())

  @Provides
  @Singleton
  fun provideMainApiRetrofit(
    gsonFactory: GsonConverterFactory,
    rxFactory: RxJava2CallAdapterFactory,
    okHttpClient: OkHttpClient,
    @ApiEndpoint endpoint: String
  ): Retrofit = Retrofit.Builder()
      .addConverterFactory(gsonFactory)
      .addCallAdapterFactory(rxFactory)
      .baseUrl(endpoint)
      .client(okHttpClient)
      .build()

}