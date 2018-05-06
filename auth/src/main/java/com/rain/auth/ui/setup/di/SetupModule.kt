package com.rain.auth.ui.setup.di

import com.rain.auth.data.AuthRepo
import com.rain.auth.ui.setup.SetupViewModel
import com.rain.auth.ui.setup.interactor.SetupInteractor
import com.rain.auth.ui.setup.reducer.SetupReducer
import com.rain.core.support.ResourceProvider
import dagger.Module
import dagger.Provides

@Module
class SetupModule {

    @SetupScope
    @Provides
    fun provideSetupViewModel(setupInteractor: SetupInteractor,
                              setupReducer: SetupReducer): SetupViewModel {
        return SetupViewModel(setupReducer, setupInteractor)
    }

    @SetupScope
    @Provides
    fun provideSetupInteractor(authRepo: AuthRepo, resourceProvider: ResourceProvider): SetupInteractor {
        return SetupInteractor(authRepo, resourceProvider)
    }

    @SetupScope
    @Provides
    fun provideSetupReducer(): SetupReducer {
        return SetupReducer()
    }
}
