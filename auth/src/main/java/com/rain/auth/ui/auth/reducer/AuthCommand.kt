package com.rain.auth.ui.auth.reducer

import com.rain.core.viewmodel.Command

sealed class AuthCommand : Command {
    class Submit(val error: String) : AuthCommand()
}
