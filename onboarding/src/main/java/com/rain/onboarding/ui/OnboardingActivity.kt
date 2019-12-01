package com.rain.onboarding.ui

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rain.onboarding.R
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_onboarding.btnGrandPermission
import kotlinx.android.synthetic.main.activity_onboarding.ivIcon
import kotlinx.android.synthetic.main.activity_onboarding.tvDescription
import javax.inject.Inject

class OnboardingActivity : AppCompatActivity(), OnboardingView {

    @Inject
    lateinit var onboardingPresenter: OnboardingPresenter

    companion object {
        const val REQUEST = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
    }

    override fun onResume() {
        super.onResume()
        onboardingPresenter.checkPermission()
    }

    override fun close() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun display(viewModel: OnboardingViewModel) {
        tvDescription.setText(viewModel.description)
        ivIcon.setImageResource(viewModel.icon)
        btnGrandPermission.setOnClickListener {
            viewModel.action.execute()
        }
    }
}
