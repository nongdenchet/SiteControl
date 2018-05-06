package com.rain.auth.data

import android.content.Context
import com.rain.auth.ui.di.SetupScope
import dagger.Module
import dagger.Provides

@Module
class AuthModule {
    private val AUTH_STORE = "AUTH_STORE"

    @SetupScope
    @Provides
    fun provideAuthRepo(authStore: AuthStore): AuthRepo {
        return AuthRepo(authStore)
    }

    @SetupScope
    @Provides
    fun provideAuthStore(context: Context): AuthStore {
        return AuthStore(context.getSharedPreferences(AUTH_STORE, Context.MODE_PRIVATE))
    }
}
