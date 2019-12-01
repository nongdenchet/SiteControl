package com.rain.sitecontrol.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rain.auth.ui.setup.SetupActivity
import com.rain.onboarding.ui.OnboardingActivity
import com.rain.sitecontrol.ui.main.MainActivity
import dagger.android.AndroidInjection
import javax.inject.Inject

class SplashActivity : AppCompatActivity(), SplashView {

    @Inject
    internal lateinit var splashPresenter: SplashPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        splashPresenter.checkPermissions()
    }

    override fun toSetup() {
        startActivityForResult(Intent(this, SetupActivity::class.java), SetupActivity.REQUEST)
    }

    override fun toOnboarding() {
        startActivityForResult(
            Intent(this, OnboardingActivity::class.java),
            OnboardingActivity.REQUEST
        )
    }

    override fun toSetting() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun close() = finish()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        splashPresenter.handleResult(requestCode, resultCode)
    }
}
