package com.rain.auth.ui.auth

import com.rain.auth.ui.auth.interactor.AuthInteractor
import com.rain.auth.ui.auth.reducer.AuthCommand
import com.rain.auth.ui.auth.reducer.AuthReducer
import com.rain.auth.ui.auth.reducer.AuthState
import com.rain.core.utils.PASSWORD_LENGTH
import com.rain.core.viewmodel.ViewModel
import io.reactivex.Observable

class AuthViewModel(reducer: AuthReducer, private val interactor: AuthInteractor)
    : ViewModel<AuthState, AuthCommand>(reducer, AuthState.INIT_STATE) {

    class Input(val password: Observable<String>)
    class Output(val error: Observable<String>, val success: Observable<Boolean>)

    fun bind(input: Input): Output {
        val command: Observable<AuthCommand> = input.password
                .filter { it.length == PASSWORD_LENGTH }
                .switchMapSingle { interactor.validate(it) }

        subscribe(command)

        return configureOutput()
    }

    private fun configureOutput(): Output {
        val error = getState()
                .map { it.error }
        val success = getState()
                .map { it.success }
                .distinctUntilChanged()

        return Output(error, success)
    }

    fun unbind() = unsubscribe()
}
