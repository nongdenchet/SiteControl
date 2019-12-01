package com.rain.service

import android.content.SharedPreferences
import androidx.annotation.MainThread
import androidx.collection.ArrayMap

private const val ENABLED = "ENABLED"

class SiteControlRepo(private val sharedPreferences: SharedPreferences) {
    private val cache = ArrayMap<String, Boolean>(100)

    @MainThread
    fun cacheResult(url: String, result: Boolean) {
        cache[url] = result
    }

    fun getResult(newUrl: String): Boolean? {
        for (url in cache.keys) {
            if (newUrl.contains(url)) {
                return cache[url]
            }
        }

        return null
    }

    fun setEnabled(value: Boolean) {
        sharedPreferences.edit()
            .putBoolean(ENABLED, value)
            .apply()
    }

    fun isEnabled(): Boolean = sharedPreferences.getBoolean(ENABLED, true)
}
