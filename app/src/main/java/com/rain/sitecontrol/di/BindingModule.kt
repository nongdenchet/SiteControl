package com.rain.sitecontrol.di

import com.rain.auth.ui.setup.SetupActivity
import com.rain.auth.ui.setup.di.SetupModule
import com.rain.auth.ui.setup.di.SetupScope
import com.rain.sitecontrol.ui.MainActivity
import com.rain.sitecontrol.ui.MainModule
import com.rain.sitecontrol.ui.MainScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BindingModule {

    @SetupScope
    @ContributesAndroidInjector(modules = [(SetupModule::class)])
    abstract fun contributeSetupActivity(): SetupActivity

    @MainScope
    @ContributesAndroidInjector(modules = [(MainModule::class)])
    abstract fun contributeMainActivity(): MainActivity
}
