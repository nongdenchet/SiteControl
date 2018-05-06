package com.rain.auth.data

import com.rain.auth.data.AuthStore.Companion.NO_PASSWORD
import com.rain.core.utils.hash256
import io.reactivex.Single

class AuthRepo(private val authStore: AuthStore) {

    fun save(password: String) {
        authStore.storeHashPassword(hash256(password))
    }

    fun hasSetup(): Boolean {
        return authStore.getHashPassword() != NO_PASSWORD
    }

    fun validate(password: String): Single<Boolean> {
        return Single.fromCallable {
            return@fromCallable authStore.getHashPassword() == hash256(password)
        }
    }
}
