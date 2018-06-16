package com.rain.onboarding.ui

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import com.rain.onboarding.permission.PermissionAction

data class OnboardingViewModel(
        @DrawableRes val icon: Int,
        @StringRes val description: Int,
        val action: PermissionAction
)
