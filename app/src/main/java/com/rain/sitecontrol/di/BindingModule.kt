package com.rain.sitecontrol.di

import com.rain.service.SiteControlModule
import com.rain.service.SiteControlScope
import com.rain.service.SiteControlService
import com.rain.sitecontrol.ui.main.MainActivity
import com.rain.sitecontrol.ui.main.MainModule
import com.rain.sitecontrol.ui.main.MainScope
import com.rain.sitecontrol.ui.splash.SplashActivity
import com.rain.sitecontrol.ui.splash.SplashModule
import com.rain.sitecontrol.ui.splash.SplashScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BindingModule {

    @MainScope
    @ContributesAndroidInjector(modules = [MainModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @SplashScope
    @ContributesAndroidInjector(modules = [SplashModule::class])
    abstract fun contributeSplashActivity(): SplashActivity

    @SiteControlScope
    @ContributesAndroidInjector(modules = [SiteControlModule::class])
    abstract fun contributeSiteControlService(): SiteControlService
}
