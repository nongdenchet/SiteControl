package com.rain.sitecontrol

import android.accessibilityservice.AccessibilityService
import android.os.Bundle
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class SiteControlService : AccessibilityService() {
    private val settingPackage = "com.android.settings"
    private val urlBarId = "com.android.chrome:id/url_bar"
    private val forceStopId = "com.android.settings:id/right_button"
    private val keyWords = setOf("porn", "sex", "xxx", "checker", "xvideo")

    override fun onInterrupt() {}

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val source = event.source ?: return
        val title = source.findAccessibilityNodeInfosByText(getString(R.string.app_name))
        val forceStopButton = source.findAccessibilityNodeInfosByViewId(forceStopId)
        if (forceStopButton.isNotEmpty() && title.isNotEmpty() && source.packageName == settingPackage) {
            performGlobalAction(GLOBAL_ACTION_BACK)
            return
        }

        val descAccessibility = source.findAccessibilityNodeInfosByText(getString(R.string.accessibility_service_description))
        if (descAccessibility.isNotEmpty() && event.packageName == settingPackage) {
            performGlobalAction(GLOBAL_ACTION_BACK)
            return
        }

        val urlBarQuery = source.findAccessibilityNodeInfosByViewId(urlBarId)
        if (urlBarQuery.isNotEmpty()) {
            preventUser(urlBarQuery[0])
        }
    }

    private fun preventUser(urlBar: AccessibilityNodeInfo) {
        for (key in keyWords) {
            if (urlBar.text.contains(key)) {
                val args = Bundle()
                args.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "")
                urlBar.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, args)
                performGlobalAction(GLOBAL_ACTION_BACK)
            }
        }
    }
}
