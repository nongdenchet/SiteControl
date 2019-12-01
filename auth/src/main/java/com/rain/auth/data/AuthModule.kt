package com.rain.auth.data

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AuthModule {
    private val AUTH_STORE = "AUTH_STORE"

    @Singleton
    @Provides
    fun provideAuthManager(authRepo: AuthRepo, context: Context): AuthManager {
        return AuthManager(authRepo, context)
    }

    @Singleton
    @Provides
    fun provideAuthRepo(authStore: AuthStore): AuthRepo {
        return AuthRepo(authStore)
    }

    @Singleton
    @Provides
    fun provideAuthStore(context: Context): AuthStore {
        return AuthStore(context.getSharedPreferences(AUTH_STORE, Context.MODE_PRIVATE))
    }
}
