package com.rain.auth.ui.reducer

import com.rain.core.viewmodel.Command

sealed class SetupCommand : Command {
    class Reset : SetupCommand()

    class Password(val data: String) : SetupCommand()

    class Confirm(val error: String) : SetupCommand()
}
