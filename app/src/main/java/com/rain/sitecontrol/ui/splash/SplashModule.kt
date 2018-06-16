package com.rain.sitecontrol.ui.splash

import android.app.Activity
import com.rain.onboarding.permission.PermissionManager
import dagger.Module
import dagger.Provides

@Module
class SplashModule {

    @SplashScope
    @Provides
    fun providePermissionManager(activity: Activity): PermissionManager {
        return PermissionManager(activity)
    }

    @SplashScope
    @Provides
    fun provideContext(activity: SplashActivity): Activity {
        return activity
    }
}
