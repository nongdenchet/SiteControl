package com.rain.sitecontrol.ui.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.rain.auth.data.AuthManager
import com.rain.auth.ui.setup.SetupActivity
import com.rain.onboarding.permission.PermissionManager
import com.rain.onboarding.ui.OnboardingActivity
import com.rain.sitecontrol.ui.main.MainActivity
import dagger.android.AndroidInjection
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var permissionManager: PermissionManager
    @Inject
    lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        if (permissionManager.hasAllPermissions()) {
            if (!authManager.hasSetup()) {
                toSetup()
            } else {
                toSetting()
            }
        } else {
            toOnboarding()
        }
    }

    private fun toSetup() {
        startActivityForResult(Intent(this, SetupActivity::class.java), SetupActivity.REQUEST)
    }

    private fun toOnboarding() {
        startActivityForResult(Intent(this, OnboardingActivity::class.java), OnboardingActivity.REQUEST)
    }

    private fun toSetting() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == OnboardingActivity.REQUEST && resultCode == Activity.RESULT_CANCELED) {
            finish()
            return
        }

        if (requestCode == SetupActivity.REQUEST && resultCode == Activity.RESULT_CANCELED) {
            finish()
            return
        }
    }
}
