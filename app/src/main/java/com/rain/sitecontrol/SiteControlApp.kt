package com.rain.sitecontrol

import android.app.Application
import com.rain.auth.ui.auth.di.AuthDialogComponentProvider
import com.rain.auth.ui.auth.di.AuthDialogModule
import com.rain.sitecontrol.di.application.AppComponent
import com.rain.sitecontrol.di.application.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

open class SiteControlApp : Application(), HasAndroidInjector, AuthDialogComponentProvider {
    private lateinit var component: AppComponent

    @Inject
    lateinit var dispatchingInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = dispatchingInjector

    override fun plus(authDialogModule: AuthDialogModule) = component.plus(authDialogModule)

    override fun onCreate() {
        super.onCreate()
        initComponent()
    }

    protected open fun initComponent() {
        component = DaggerAppComponent.builder()
                .application(this)
                .build()
        component.inject(this)
    }
}
