package com.rain.auth.ui.auth.interactor

import com.rain.auth.R
import com.rain.auth.data.AuthRepo
import com.rain.auth.ui.auth.reducer.AuthCommand
import com.rain.core.support.ResourceProvider
import com.rain.core.utils.EMPTY_STRING
import io.reactivex.Single

class AuthInteractor(
    private val authRepo: AuthRepo,
    private val resourceProvider: ResourceProvider
) {

    fun validate(password: String): Single<AuthCommand.Submit> {
        return authRepo.validate(password)
            .map {
                if (it) EMPTY_STRING
                else resourceProvider.getString(R.string.invalid_password)
            }
            .map { AuthCommand.Submit(it) }
    }
}
