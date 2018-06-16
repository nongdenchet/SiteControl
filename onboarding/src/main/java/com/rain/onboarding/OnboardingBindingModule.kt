package com.rain.onboarding

import com.rain.onboarding.ui.OnboardingActivity
import com.rain.onboarding.ui.OnboardingModule
import com.rain.onboarding.ui.OnboardingScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class OnboardingBindingModule {

    @OnboardingScope
    @ContributesAndroidInjector(modules = [OnboardingModule::class])
    abstract fun contributeOnboardingActivity(): OnboardingActivity
}
