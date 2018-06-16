package com.rain.sitecontrol.di.application

import android.content.ClipboardManager
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson
import com.rain.auth.data.AuthModule
import com.rain.core.support.ResourceProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [AuthModule::class])
class AppModule {

    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideSharePreference(context: Context): SharedPreferences {
        return context.getSharedPreferences(context.packageName, MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideInputMethodManager(context: Context): InputMethodManager {
        return context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    @Provides
    @Singleton
    fun providePackageManager(context: Context): PackageManager {
        return context.packageManager
    }

    @Provides
    @Singleton
    fun provideResourceProvider(context: Context): ResourceProvider {
        return ResourceProvider(context.resources)
    }

    @Provides
    @Singleton
    fun provideClipboardManager(context: Context): ClipboardManager {
        return context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }
}
