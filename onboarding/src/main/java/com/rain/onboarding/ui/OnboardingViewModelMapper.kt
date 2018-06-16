package com.rain.onboarding.ui

import com.rain.onboarding.R
import com.rain.onboarding.permission.PermissionAction

class OnboardingViewModelMapper {

    fun toItem(action: PermissionAction): OnboardingViewModel {
        return when (action) {
            is PermissionAction.Accessibility -> OnboardingViewModel(
                    R.drawable.ic_accessibility,
                    R.string.accessibility_description,
                    action
            )
            is PermissionAction.Overlay -> OnboardingViewModel(
                    R.drawable.ic_overlay,
                    R.string.overlay_description,
                    action
            )
        }
    }
}
