package com.dlet.cartrack.challenge.di

import com.dlet.cartrack.challenge.di.module.ApplicationModule
import com.dlet.cartrack.challenge.di.module.DataModule
import com.dlet.cartrack.challenge.di.module.ManagerModule
import com.dlet.cartrack.challenge.di.module.ViewModelModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@InstallIn(ApplicationComponent::class)
@Module(
    includes = [
      ApplicationModule::class,
      ManagerModule::class,
      DataModule::class,
      ViewModelModule::class
    ]
)
interface MainAggregatorModule