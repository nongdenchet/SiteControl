package com.rain.sitecontrol.di

import com.rain.auth.SetupActivity
import com.rain.auth.SetupModule
import com.rain.auth.SetupScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BindingModule {

    @SetupScope
    @ContributesAndroidInjector(modules = [(SetupModule::class)])
    abstract fun contributeSetupActivity(): SetupActivity
}
