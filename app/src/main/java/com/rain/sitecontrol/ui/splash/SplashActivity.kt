package com.rain.sitecontrol.ui.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.rain.onboarding.permission.PermissionManager
import com.rain.onboarding.ui.OnboardingActivity
import com.rain.sitecontrol.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {
    private val permissionManager = PermissionManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (permissionManager.hasAllPermissions()) {
            finishOnboarding()
        } else {
            toOnboarding()
        }
    }

    private fun toOnboarding() {
        startActivityForResult(Intent(this, OnboardingActivity::class.java), OnboardingActivity.REQUEST)
    }

    private fun finishOnboarding() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            finishOnboarding()
        } else {
            finish()
        }
    }
}
