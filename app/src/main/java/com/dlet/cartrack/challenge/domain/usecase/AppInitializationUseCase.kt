package com.dlet.cartrack.challenge.domain.usecase

interface AppInitializationUseCase {

  fun init()

  fun onAppCreated()

  fun onAppDestroyed()

}