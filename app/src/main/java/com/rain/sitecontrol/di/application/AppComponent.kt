package com.rain.sitecontrol.di.application

import android.content.Context
import android.content.SharedPreferences
import com.rain.auth.ui.AuthBindingModule
import com.rain.auth.ui.auth.di.AuthDialogComponentProvider
import com.rain.onboarding.OnboardingBindingModule
import com.rain.sitecontrol.SiteControlApp
import com.rain.sitecontrol.di.BindingModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    BindingModule::class,
    AuthBindingModule::class,
    OnboardingBindingModule::class
])
interface AppComponent : AuthDialogComponentProvider {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(context: Context): Builder

        fun build(): AppComponent
    }

    fun inject(app: SiteControlApp)

    fun getSharePreferences(): SharedPreferences
}
