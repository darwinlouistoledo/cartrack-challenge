package com.dlet.cartrack.challenge.module.map

import com.dlet.cartrack.challenge.common_android.mvi.*
import com.dlet.cartrack.challenge.domain.common.SingleEvent
import com.dlet.cartrack.challenge.domain.model.User
import com.dlet.cartrack.challenge.domain.rx.SchedulerProvider
import com.dlet.cartrack.challenge.domain.sealedclass.DataResult
import com.dlet.cartrack.challenge.domain.usecase.UserUseCase
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import javax.inject.Inject

class MapUsersViewModel @Inject constructor(
  userUseCase: UserUseCase,
  schedulerProvider: SchedulerProvider
) : MviViewModel<MapUsersViewModel.Action, MapUsersViewModel.State>() {

  sealed class Action : MviAction {
    object LoadUsers : Action()
  }

  sealed class Change : MviChange {
    data class UserList(val userList: List<User>) : Change()

    data class Error(val throwable: Throwable) : Change()
  }

  data class State(
    val userList: List<User> = emptyList(),
    val error: SingleEvent<Throwable>? = null
  ) : MviState

  override val initialState: State
    get() = State()

  private val reducer: Reducer<State, Change> = { state, change ->
    when (change) {
      is Change.UserList -> state.copy(userList = change.userList)
      is Change.Error -> state.copy(
        error = SingleEvent(change.throwable)
      )
    }
  }

  init {
    val actionLoadUsersObs = actions.ofType<Action.LoadUsers>()
      .switchMap {
        userUseCase.getUsers()
          .map {
            when (it) {
              is DataResult.Success -> Change.UserList(it.value)
              is DataResult.Failed -> Change.Error(it.error)
            }
          }
          .onErrorReturn { Change.Error(it) }
          .subscribeOn(schedulerProvider.io())
          .observeOn(schedulerProvider.ui())
      }


    val states = Observable.mergeArray(
      actionLoadUsersObs
    )
      .onErrorReturn { Change.Error(it) }
      .scan(initialState, reducer)
      .distinctUntilChanged()

    subscribe(states)

  }
}