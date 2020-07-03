package com.dlet.cartrack.challenge.domain.usecase

import com.dlet.cartrack.challenge.domain.model.User
import com.dlet.cartrack.challenge.domain.sealedclass.DataResult
import io.reactivex.Observable

interface UserUseCase {
  fun getUsers(): Observable<DataResult<List<User>>>
}