package com.rain.auth.ui.auth.di

import com.rain.auth.data.AuthRepo
import com.rain.auth.ui.auth.AuthViewModel
import com.rain.auth.ui.auth.interactor.AuthInteractor
import com.rain.auth.ui.auth.reducer.AuthReducer
import com.rain.core.support.ResourceProvider
import dagger.Module
import dagger.Provides

@Module
class AuthDialogModule {

    @AuthDialogScope
    @Provides
    fun provideAuthViewModel(authReducer: AuthReducer, authInteractor: AuthInteractor): AuthViewModel {
        return AuthViewModel(authReducer, authInteractor)
    }

    @AuthDialogScope
    @Provides
    fun provideAuthReducer(): AuthReducer {
        return AuthReducer()
    }

    @AuthDialogScope
    @Provides
    fun provideAuthInteractor(authRepo: AuthRepo, resourceProvider: ResourceProvider): AuthInteractor {
        return AuthInteractor(authRepo, resourceProvider)
    }
}
