package com.dlet.cartrack.challenge.module.login

import com.dlet.cartrack.challenge.common_android.mvi.*
import com.dlet.cartrack.challenge.domain.common.SingleEvent
import com.dlet.cartrack.challenge.domain.model.*
import com.dlet.cartrack.challenge.domain.rx.SchedulerProvider
import com.dlet.cartrack.challenge.domain.sealedclass.DataResult
import com.dlet.cartrack.challenge.domain.usecase.CountriesUseCase
import com.dlet.cartrack.challenge.domain.usecase.LogInUseCase
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import okhttp3.internal.applyConnectionSpec
import timber.log.Timber
import javax.inject.Inject

class LogInViewModel @Inject constructor(
  private val logInUseCase: LogInUseCase,
  countriesUseCase: CountriesUseCase,
  private val schedulerProvider: SchedulerProvider
) : MviViewModel<LogInViewModel.Action, LogInViewModel.State>() {

  sealed class Action : MviAction {
    data class Login(val username: String, val password: String) : Action()

    data class LoadCountry(val rawJson: String, val localeCountry: String) :Action()

    data class SetCountry(val country: Country): Action()
  }

  sealed class Change : MviChange {
    object SuccessLogin : Change()

    data class SetCountry(val country: Country) : Change()

    data class Loading(val boolean: Boolean) : Change()

    data class InvalidLogin(val error: Throwable) : Change()

    data class Error(val error: Throwable) : Change()
  }

  data class State(
    val country: Country ?= null,
    val successLogin: SingleEvent<Boolean>? = null,
    val showLoading: SingleEvent<Boolean>? = null,
    val invalidLogin: SingleEvent<Throwable>? = null,
    val error: SingleEvent<Throwable>? = null
  ) : MviState

  override val initialState: State
    get() = State()

  private val reducer: Reducer<State, Change> = { state, change ->
    when(change){
      Change.SuccessLogin -> state.copy(successLogin = SingleEvent(true), showLoading = SingleEvent(false))
      is Change.SetCountry -> state.copy(country = change.country)
      is Change.InvalidLogin -> state.copy(invalidLogin = SingleEvent(change.error))
      is Change.Loading -> state.copy(showLoading = SingleEvent(change.boolean))
      is Change.Error -> state.copy(error = SingleEvent(change.error))
    }
  }

  init {
    val setCountryAction = actions.ofType<Action.LoadCountry>()
      .switchMap {
        countriesUseCase.getCountryOfLocale(it.rawJson, it.localeCountry)
          .toObservable()
          .map {result->
            when(result){
              is DataResult.Success -> Change.SetCountry(result.value)
              is DataResult.Failed -> Change.InvalidLogin(result.error)
            }
          }
          .startWith(listOf(Change.Loading(true)))
          .onErrorReturn { Change.Error(it) }
          .subscribeOn(schedulerProvider.io())
          .observeOn(schedulerProvider.ui())
      }

    val loginAction = actions.ofType<Action.Login>()
      .switchMap {
        logInUseCase.doLogIn(it.username, it.password)
          .toObservable()
          .map {result->
            when(result){
              is DataResult.Success -> Change.SuccessLogin
              is DataResult.Failed -> Change.InvalidLogin(result.error)
            }
          }
          .startWith(listOf(Change.Loading(true)))
          .onErrorReturn { Change.Error(it) }
          .subscribeOn(schedulerProvider.io())
          .observeOn(schedulerProvider.ui())
      }

    val setSelectedCountryAction = actions.ofType<Action.SetCountry>()
      .map { Change.SetCountry(it.country) }

    val createTestUserOneObs = logInUseCase.createTestUserAccount(
      User(
        id = 1000,
        name = "Juan Cruz",
        username = "juancruz",
        email = "juancruz@.test.com",
        password = "pass1234",
        phone = "123-4567",
        website = "juancruz.ph",
        address = Address(
          street = null,
          city = null,
          zipCode = null,
          suite = null,
          geoLocation = GeoLocation(0f, 0f)
        ),
        company = Company(
          name = "Juancruz Inc",
          catchPhrase = "Juancruz is awesome",
          bs = "Juancruz-is-awesome"
        )
      )
    )
      .toObservable()
      .take(1)
      .map {result->
        when(result){
          is DataResult.Success -> {
            Timber.i("Test user created.")
            Change.Loading(false)
          }
          is DataResult.Failed -> Change.InvalidLogin(result.error)
        }
      }
      .onErrorReturn { Change.Error(it) }
      .subscribeOn(schedulerProvider.io())
      .observeOn(schedulerProvider.ui())

    val createTestUserTwoObs = logInUseCase.createTestUserAccount(
      User(
        id = 1001,
        name = "John Doe",
        username = "johndoe",
        email = "johndoe@.test.com",
        password = "pass4321",
        phone = "123-4567",
        website = "johndoe.com",
        address = Address(
          street = null,
          city = null,
          zipCode = null,
          suite = null,
          geoLocation = GeoLocation(0f, 0f)
        ),
        company = Company(
          name = "Johndoe Inc",
          catchPhrase = "Johndoe is awesome",
          bs = "johndoe-is-awesome"
        )
      )
    )
      .toObservable()
      .take(1)
      .map {result->
        when(result){
          is DataResult.Success -> {
            Timber.i("Test user created.")
            Change.Loading(false)
          }
          is DataResult.Failed -> Change.InvalidLogin(result.error)
        }
      }
      .onErrorReturn { Change.Error(it) }
      .subscribeOn(schedulerProvider.io())
      .observeOn(schedulerProvider.ui())

    val states = Observable.mergeArray(
      setCountryAction,
      loginAction,
      setSelectedCountryAction,
      createTestUserOneObs,
      createTestUserTwoObs
    )
      .onErrorReturn { Change.Error(it) }
      .scan(initialState, reducer)
      .distinctUntilChanged()

    subscribe(states)
  }
}