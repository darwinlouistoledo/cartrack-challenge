package com.dlet.cartrack.challenge.module.users

import com.dlet.cartrack.challenge.common_android.mvi.*
import com.dlet.cartrack.challenge.domain.common.SingleEvent
import com.dlet.cartrack.challenge.domain.model.User
import com.dlet.cartrack.challenge.domain.rx.SchedulerProvider
import com.dlet.cartrack.challenge.domain.sealedclass.DataResult
import com.dlet.cartrack.challenge.domain.usecase.UserUseCase
import io.reactivex.Observable
import javax.inject.Inject

class UserListViewModel @Inject constructor(
  userUseCase: UserUseCase,
  schedulerProvider: SchedulerProvider
): MviViewModel<UserListViewModel.Action, UserListViewModel.State>(){

  sealed class Action: MviAction{

  }

  sealed class Change: MviChange{
    data class UserList(val userList: List<User>): Change()

    data class IsLoading(val bool: Boolean): Change()

    data class Error(val throwable: Throwable): Change()
  }

  data class State(
    val userList: List<User> = emptyList(),
    val isLoading: SingleEvent<Boolean>?=null,
    val error: SingleEvent<Throwable>?=null
  ): MviState

  override val initialState: State
    get() = State()

  private val reducer: Reducer<State, Change> = {state, change ->
    when(change){
      is Change.UserList -> state.copy(userList = change.userList, isLoading = SingleEvent(false))
      is Change.IsLoading -> state.copy(isLoading = SingleEvent(change.bool))
      is Change.Error -> state.copy(error = SingleEvent(change.throwable), isLoading = SingleEvent(false))
    }
  }

  init {
    val actionLoadUsersObs = userUseCase.getUsers()
      .map {
        when(it){
          is DataResult.Success -> Change.UserList(it.value)
          is DataResult.Failed -> Change.Error(it.error)
        }
      }
      .startWith(listOf(Change.IsLoading(true)))
      .onErrorReturn { Change.Error(it) }
      .subscribeOn(schedulerProvider.io())
      .observeOn(schedulerProvider.ui())

    val states = Observable.mergeArray(
      actionLoadUsersObs
    )
      .onErrorReturn { Change.Error(it) }
      .scan(initialState, reducer)
      .distinctUntilChanged()

    subscribe(states)

  }
}