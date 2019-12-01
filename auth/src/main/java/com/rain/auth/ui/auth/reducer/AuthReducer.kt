package com.rain.auth.ui.auth.reducer

import com.rain.core.utils.EMPTY_STRING
import com.rain.core.viewmodel.Reducer

class AuthReducer : Reducer<AuthState, AuthCommand> {
    override fun apply(prev: AuthState, command: AuthCommand): AuthState {
        return when (command) {
            is AuthCommand.Submit -> password(prev, command.error)
        }
    }

    private fun password(prev: AuthState, error: String): AuthState {
        if (error.isNotBlank()) {
            return prev.copy(success = false, error = error)
        }

        return prev.copy(success = true, error = EMPTY_STRING)
    }
}
