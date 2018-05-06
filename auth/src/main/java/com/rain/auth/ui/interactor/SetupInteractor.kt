package com.rain.auth.ui.interactor

import com.rain.auth.R
import com.rain.auth.data.AuthRepo
import com.rain.auth.ui.reducer.SetupCommand
import com.rain.core.support.ResourceProvider
import com.rain.core.utils.EMPTY_STRING
import io.reactivex.Single

class SetupInteractor(private val authRepo: AuthRepo, private val resourceProvider: ResourceProvider) {

    fun confirmPassword(password: String? = EMPTY_STRING, confirm: String): Single<SetupCommand.Confirm> {
        return Single.fromCallable {
            if (password == confirm) {
                return@fromCallable SetupCommand.Confirm(EMPTY_STRING)
            }
            return@fromCallable SetupCommand.Confirm(resourceProvider.getString(R.string.confirm_not_match))
        }.doOnSuccess {
            if (it.error.isNotBlank()) {
                authRepo.save(confirm)
            }
        }
    }
}
