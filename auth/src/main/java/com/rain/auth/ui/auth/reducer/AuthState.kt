package com.rain.auth.ui.auth.reducer

import com.rain.core.utils.EMPTY_STRING
import com.rain.core.viewmodel.ViewState

data class AuthState(val success: Boolean = false, val error: String = EMPTY_STRING) : ViewState {
    companion object {
        val INIT_STATE = AuthState()
    }
}
