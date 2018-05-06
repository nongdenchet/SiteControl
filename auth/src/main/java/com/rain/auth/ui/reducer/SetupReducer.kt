package com.rain.auth.ui.reducer

import com.rain.auth.R
import com.rain.core.utils.EMPTY_STRING
import com.rain.core.viewmodel.Reducer

class SetupReducer : Reducer<SetupState, SetupCommand> {

    override fun apply(prev: SetupState, command: SetupCommand): SetupState {
        return when (command) {
            is SetupCommand.Confirm -> confirm(prev, command.error)
            is SetupCommand.Reset -> reset()
            is SetupCommand.Password -> password(prev, command.data)
        }
    }

    private fun password(prev: SetupState, data: String): SetupState {
        return prev.copy(
                step = SetupState.Step.Confirmation,
                title = R.string.confirm_pin,
                password = data,
                error = EMPTY_STRING
        )
    }

    private fun reset(): SetupState {
        return SetupState.INIT_STATE
    }

    private fun confirm(prev: SetupState, error: String): SetupState {
        if (error.isNotBlank()) {
            return prev.copy(error = error)
        }

        return prev.copy(step = SetupState.Step.Success, error = error)
    }
}
