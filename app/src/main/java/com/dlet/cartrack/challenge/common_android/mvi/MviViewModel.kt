package com.dlet.cartrack.challenge.common_android.mvi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

abstract class MviViewModel<A : MviAction, S : MviState> : ViewModel() {

  protected abstract val initialState: S

  protected val actions: PublishSubject<A> = PublishSubject.create()

  private val disposables: CompositeDisposable = CompositeDisposable()

  private val state = MutableLiveData<S>()

  private val tag by lazy { javaClass.simpleName }

  val observableState: LiveData<S> = MediatorLiveData<S>().apply {
    addSource(state) { data ->
      Timber.d("$tag received state: $data")
      value = data
    }
  }

  fun dispatch(action: A) {
    Timber.d("$tag received action: $action")
    actions.onNext(action)
  }

  protected fun subscribe(observable: Observable<S>) {
    disposables += observable.subscribe(state::postValue, Timber::e)
  }

  override fun onCleared() {
    super.onCleared()
    disposables.clear()
  }

}