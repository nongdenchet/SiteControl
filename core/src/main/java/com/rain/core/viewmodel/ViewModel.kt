package com.rain.core.viewmodel

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

interface ViewState
interface Command
interface Reducer<T : ViewState, E : Command> : BiFunction<T, E, T>

abstract class ViewModel<T : ViewState, E : Command>(private val reducer: Reducer<T, E>, private val initState: T) {
    private val state = BehaviorRelay.createDefault<T>(initState)
    private var disposable: Disposable? = null

    fun subscribe(commands: Observable<E>) {
        disposable = commands.scan(initState, reducer)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ state.accept(it) }, Timber::e)
    }

    protected fun getState(): Observable<T> {
        return state.hide().serialize()
    }

    protected fun getCurrentState(): T {
        return state.value
    }

    fun unsubscribe() {
        disposable?.dispose()
        disposable = null
    }
}
