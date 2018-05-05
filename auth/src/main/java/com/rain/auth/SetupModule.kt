package com.rain.auth

import dagger.Module
import dagger.Provides

@Module
class SetupModule {

    @SetupScope
    @Provides
    fun provideSetupViewModel(): SetupViewModel {
        return SetupViewModel()
    }
}
