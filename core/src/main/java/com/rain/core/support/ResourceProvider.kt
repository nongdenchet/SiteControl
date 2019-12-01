package com.rain.core.support

import android.content.res.Resources
import androidx.annotation.StringRes

class ResourceProvider(private val resources: Resources) {

    fun getString(@StringRes stringId: Int): String {
        return resources.getString(stringId)
    }
}
