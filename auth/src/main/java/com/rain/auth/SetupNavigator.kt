package com.rain.auth

import android.content.Context
import android.content.Intent

class SetupNavigator(private val context: Context) {

    fun navigate() {
        context.startActivity(Intent(context, SetupActivity::class.java))
    }
}
