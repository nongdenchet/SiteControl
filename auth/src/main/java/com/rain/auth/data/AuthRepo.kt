package com.rain.auth.data

import com.rain.core.utils.hash256
import io.reactivex.Completable
import io.reactivex.Single

class AuthRepo(private val authStore: AuthStore) {

    fun save(password: String): Completable {
        return Completable.fromAction {
            authStore.storeHashPassword(hash256(password))
        }
    }

    fun validate(password: String): Single<Boolean> {
        return Single.fromCallable {
            return@fromCallable authStore.getHashPassword() == hash256(password)
        }
    }
}
