package com.rain.auth.data

import android.content.SharedPreferences
import com.rain.core.utils.EMPTY_STRING

class AuthStore(private val sharedPreferences: SharedPreferences) {
    companion object {
        const val HASH_PASSWORD = "HASH_PASSWORD"
        const val NO_PASSWORD = "NO_PASSWORD"
    }

    fun getHashPassword(): String {
        return sharedPreferences.getString(HASH_PASSWORD, NO_PASSWORD) ?: EMPTY_STRING
    }

    fun storeHashPassword(hash: String) {
        sharedPreferences.edit()
            .putString(HASH_PASSWORD, hash)
            .apply()
    }
}
