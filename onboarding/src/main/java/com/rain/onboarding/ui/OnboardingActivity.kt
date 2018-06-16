package com.rain.onboarding.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.rain.onboarding.R
import com.rain.onboarding.permission.PermissionManager
import kotlinx.android.synthetic.main.activity_onboarding.btnGrandPermission
import kotlinx.android.synthetic.main.activity_onboarding.ivIcon
import kotlinx.android.synthetic.main.activity_onboarding.tvDescription

class OnboardingActivity : AppCompatActivity() {
    private val permissionManager = PermissionManager(this)
    private val permissionItemMapper = OnboardingViewModelMapper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
    }

    override fun onResume() {
        super.onResume()
        val permissions = permissionManager.checkPermissions()
                .map { permissionItemMapper.toItem(it) }

        if (permissions.isEmpty()) {
            finish()
        } else {
            tvDescription.setText(permissions[0].description)
            ivIcon.setImageResource(permissions[0].icon)
            btnGrandPermission.setOnClickListener {
                permissions[0].action.execute()
            }
        }
    }
}
