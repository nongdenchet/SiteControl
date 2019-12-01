package com.rain.onboarding.ui

import com.rain.onboarding.R
import com.rain.onboarding.permission.PermissionAction
import com.rain.onboarding.permission.PermissionManager

class OnboardingPresenter(
    private val permissionManager: PermissionManager,
    private val onboardingView: OnboardingView
) {
    fun checkPermission() {
        val permissions = permissionManager.checkPermissions()
            .map { toViewModel(it) }
        if (permissions.isEmpty()) {
            onboardingView.close()
        } else {
            onboardingView.display(permissions[0])
        }
    }

    private fun toViewModel(action: PermissionAction): OnboardingViewModel {
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
