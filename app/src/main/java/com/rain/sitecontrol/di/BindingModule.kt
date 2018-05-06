package com.rain.sitecontrol.di

import com.rain.auth.ui.SetupActivity
import com.rain.auth.ui.di.SetupModule
import com.rain.auth.ui.di.SetupScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BindingModule {

    @SetupScope
    @ContributesAndroidInjector(modules = [(SetupModule::class)])
    abstract fun contributeSetupActivity(): SetupActivity
}
