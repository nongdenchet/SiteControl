package com.rain.sitecontrol.ui.splash

import android.app.Activity
import com.rain.auth.data.AuthManager
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
    fun provideActivity(activity: SplashActivity): Activity {
        return activity
    }

    @SplashScope
    @Provides
    fun provideSplashView(activity: SplashActivity): SplashView {
        return activity
    }

    @SplashScope
    @Provides
    fun provideSplashPresenter(permissionManager: PermissionManager,
                               authManager: AuthManager,
                               splashView: SplashView): SplashPresenter {
        return SplashPresenter(permissionManager, authManager, splashView)
    }
}
