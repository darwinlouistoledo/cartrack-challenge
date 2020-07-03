package com.dlet.cartrack.challenge.data.remote.service

import com.dlet.cartrack.challenge.data.remote.repo.UserRepo
import io.reactivex.Single
import retrofit2.http.GET

interface UserApiService {

  @GET("users")
  fun getUserList(): Single<List<UserRepo>>
}