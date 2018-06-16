package com.rain.onboarding.permission

import android.content.Context
import com.rain.core.utils.toAccessibilityPermission
import com.rain.core.utils.toOverlayPermission

sealed class PermissionAction {
    abstract fun execute()

    class Overlay(private val context: Context) : PermissionAction() {
        override fun execute() = toOverlayPermission(context)
    }

    class Accessibility(private val context: Context) : PermissionAction() {
        override fun execute() = toAccessibilityPermission(context)
    }
}
