package com.dlet.cartrack.challenge.module.countries

import com.dlet.cartrack.challenge.common_android.mvi.*
import com.dlet.cartrack.challenge.domain.common.SingleEvent
import com.dlet.cartrack.challenge.domain.model.Country
import com.dlet.cartrack.challenge.domain.rx.SchedulerProvider
import com.dlet.cartrack.challenge.domain.sealedclass.DataResult
import com.dlet.cartrack.challenge.domain.usecase.CountriesUseCase
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import javax.inject.Inject

class CountryListViewModel @Inject constructor(
  countriesUseCase: CountriesUseCase,
  private val schedulerProvider: SchedulerProvider
) : MviViewModel<CountryListViewModel.Action, CountryListViewModel.State>() {

  sealed class Action : MviAction {
    data class LoadCountries(val rawJson: String) : Action()
  }

  sealed class Change : MviChange {
    data class CountryList(val countryList: List<Country>) : Change()

    data class Error(val error: Throwable) : Change()
  }

  data class State(
    val countryList: List<Country> = emptyList(),
    val error: SingleEvent<Throwable>? = null
  ) : MviState

  override val initialState: State
    get() = State()

  private val reducer: Reducer<State, Change> = { state, change ->
    when (change) {
      is Change.CountryList -> state.copy(countryList = change.countryList)
      is Change.Error -> state.copy(error = SingleEvent(change.error))
    }
  }

  init {
    val countriesAction = actions.ofType<Action.LoadCountries>()
      .switchMap { countriesUseCase.getAllCountry(it.rawJson)
        .map { result ->
          when (result) {
            is DataResult.Success -> Change.CountryList(result.value)
            is DataResult.Failed -> Change.Error(result.error)
          }
        }
        .toObservable()
        .onErrorReturn { Change.Error(it) }
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.ui()) }




    val states = Observable.mergeArray(
      countriesAction
    )
      .onErrorReturn { Change.Error(it) }
      .scan(initialState, reducer)
      .distinctUntilChanged()

    subscribe(states)
  }
}