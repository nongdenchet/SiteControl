package com.rain.sitecontrol.service

import androidx.annotation.MainThread
import androidx.collection.ArrayMap

class SiteControlRepo {
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
}