package com.rain.sitecontrol

import android.app.Activity
import android.app.Application
import android.app.Service
import com.rain.sitecontrol.di.application.AppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import com.rain.sitecontrol.di.application.DaggerAppComponent
import javax.inject.Inject

open class SiteControlApp : Application(), HasActivityInjector, HasServiceInjector {
    lateinit var component: AppComponent

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var dispatchingServiceInjector: DispatchingAndroidInjector<Service>

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingActivityInjector
    }

    override fun serviceInjector(): AndroidInjector<Service> {
        return dispatchingServiceInjector
    }

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
