package com.rain.onboarding.ui

import android.app.Activity
import com.rain.onboarding.permission.PermissionManager
import dagger.Module
import dagger.Provides

@Module
class OnboardingModule {

    @OnboardingScope
    @Provides
    fun provideActivity(activity: OnboardingActivity): Activity {
        return activity
    }

    @OnboardingScope
    @Provides
    fun provideOnboardingView(activity: OnboardingActivity): OnboardingView {
        return activity
    }

    @OnboardingScope
    @Provides
    fun providePermissionManager(activity: OnboardingActivity): PermissionManager {
        return PermissionManager(activity)
    }

    @OnboardingScope
    @Provides
    fun provideOnboardingPresenter(onboardingView: OnboardingView,
                                   permissionManager: PermissionManager): OnboardingPresenter {
        return OnboardingPresenter(permissionManager, onboardingView)
    }
}
