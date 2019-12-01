package com.rain.auth.ui.setup

import com.rain.auth.ui.setup.interactor.SetupInteractor
import com.rain.auth.ui.setup.reducer.SetupCommand
import com.rain.auth.ui.setup.reducer.SetupReducer
import com.rain.auth.ui.setup.reducer.SetupState
import com.rain.auth.ui.setup.reducer.SetupState.Step
import com.rain.core.utils.PASSWORD_LENGTH
import com.rain.core.viewmodel.ViewModel
import io.reactivex.Observable

class SetupViewModel(reducer: SetupReducer, private val interactor: SetupInteractor)
    : ViewModel<SetupState, SetupCommand>(reducer, SetupState.INIT_STATE) {

    class Input(
            val password: Observable<String>,
            val confirm: Observable<String>,
            val resetClicks: Observable<Any>
    )

    class Output(
            val error: Observable<String>,
            val canGoBack: Observable<Boolean>,
            val title: Observable<Int>,
            val step: Observable<Step>
    )

    fun bind(input: Input): Output {
        val passwordCommand = input.password
                .filter { it.length == PASSWORD_LENGTH }
                .filter { getCurrentState().step == Step.Init }
                .map { SetupCommand.Password(it) }
        val confirmCommand = input.confirm
                .filter { it.length == PASSWORD_LENGTH }
                .filter { getCurrentState().step == Step.Confirmation }
                .switchMapSingle { interactor.confirmPassword(getCurrentState().password, it) }
        val resetCommand = input.resetClicks
                .map { SetupCommand.Reset() }

        subscribe(Observable.merge(passwordCommand, confirmCommand, resetCommand))

        return configureOutput()
    }

    private fun configureOutput(): Output {
        val error = getState()
                .map { it.error }
        val canGoBack = getState()
                .map { it.step }
                .map { it == Step.Confirmation }
                .distinctUntilChanged()
        val title = getState()
                .map { it.title }
                .distinctUntilChanged()
        val step = getState()
                .map { it.step }
                .distinctUntilChanged()

        return Output(error, canGoBack, title, step)
    }

    fun unbind() = unsubscribe()
}
