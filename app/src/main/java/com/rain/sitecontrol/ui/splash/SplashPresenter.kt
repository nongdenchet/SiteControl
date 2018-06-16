package com.rain.sitecontrol.ui.splash

import android.app.Activity
import com.rain.auth.data.AuthManager
import com.rain.auth.ui.setup.SetupActivity
import com.rain.onboarding.permission.PermissionManager
import com.rain.onboarding.ui.OnboardingActivity

class SplashPresenter(private val permissionManager: PermissionManager,
                      private val authManager: AuthManager,
                      private val splashView: SplashView) {

    fun checkPermissions() {
        if (permissionManager.hasAllPermissions()) {
            if (!authManager.hasSetup()) {
                splashView.toSetup()
            } else {
                splashView.toSetting()
                splashView.close()
            }
        } else {
            splashView.toOnboarding()
        }
    }

    fun handleResult(requestCode: Int, resultCode: Int) {
        if (requestCode == OnboardingActivity.REQUEST && resultCode == Activity.RESULT_CANCELED) {
            splashView.close()
            return
        }

        if (requestCode == SetupActivity.REQUEST && resultCode == Activity.RESULT_CANCELED) {
            splashView.close()
            return
        }
    }
}
