package com.rain.auth.ui.reducer

import android.support.annotation.StringRes
import com.rain.auth.R
import com.rain.core.utils.EMPTY_STRING
import com.rain.core.viewmodel.ViewState

data class SetupState(val step: Step = Step.Init,
                      @StringRes val title: Int = R.string.setup_pin,
                      val password: String = EMPTY_STRING,
                      val error: String = EMPTY_STRING) : ViewState {
    enum class Step {
        Init, Confirmation, Success
    }

    companion object {
        val INIT_STATE = SetupState()
    }
}
