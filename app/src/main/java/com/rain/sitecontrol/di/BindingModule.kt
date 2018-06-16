package com.rain.sitecontrol.di

import com.rain.sitecontrol.ui.main.MainActivity
import com.rain.sitecontrol.ui.main.MainModule
import com.rain.sitecontrol.ui.main.MainScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BindingModule {

    @MainScope
    @ContributesAndroidInjector(modules = [(MainModule::class)])
    abstract fun contributeMainActivity(): MainActivity
}
