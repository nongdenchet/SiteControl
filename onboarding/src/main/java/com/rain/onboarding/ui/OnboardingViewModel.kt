package com.rain.onboarding.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.rain.onboarding.permission.PermissionAction

data class OnboardingViewModel(
        @DrawableRes val icon: Int,
        @StringRes val description: Int,
        val action: PermissionAction
)
