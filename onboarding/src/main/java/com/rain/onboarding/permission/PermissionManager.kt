package com.rain.onboarding.permission

import android.content.Context
import com.rain.core.utils.hasAccessibilityPermission
import com.rain.core.utils.hasOverlayPermission

class PermissionManager(private val context: Context) {

    fun hasAllPermissions() = checkPermissions().isEmpty()

    fun checkPermissions(): List<PermissionAction> {
        val permissions = mutableListOf<PermissionAction>()

        if (!hasAccessibilityPermission(context)) {
            permissions.add(PermissionAction.Accessibility(context))
        }

        if (!hasOverlayPermission(context)) {
            permissions.add(PermissionAction.Overlay(context))
        }

        return permissions.toList()
    }
}
