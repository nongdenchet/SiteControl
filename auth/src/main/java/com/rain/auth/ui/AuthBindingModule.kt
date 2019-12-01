package com.rain.auth.ui

import com.rain.auth.ui.setup.SetupActivity
import com.rain.auth.ui.setup.di.SetupModule
import com.rain.auth.ui.setup.di.SetupScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AuthBindingModule {

    @SetupScope
    @ContributesAndroidInjector(modules = [SetupModule::class])
    abstract fun contributeSetupActivity(): SetupActivity
}
