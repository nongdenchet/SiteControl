package com.rain.core.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Switch
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxCompoundButton
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

fun hasOverlayPermission(context: Context): Boolean {
    return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(context.applicationContext)
}

fun hasAccessibilityPermission(context: Context): Boolean {
    return Settings.Secure.getInt(
        context.applicationContext.contentResolver,
        Settings.Secure.ACCESSIBILITY_ENABLED
    ) == 1
}

fun toOverlayPermission(context: Context) {
    if (!hasOverlayPermission(context)) {
        val intent = Intent()
        intent.action = ACTION_MANAGE_OVERLAY_PERMISSION
        intent.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK
                    or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
        )
        intent.data = Uri.parse("package:${context.packageName}")
        context.startActivity(intent)
    }
}

fun toAccessibilityPermission(context: Context) {
    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    intent.addFlags(
        Intent.FLAG_ACTIVITY_NEW_TASK
                or Intent.FLAG_ACTIVITY_CLEAR_TASK
                or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
    )
    context.startActivity(intent)
}

@Suppress("DEPRECATION")
fun getOverlayType(): Int {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        return WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
    }
    return WindowManager.LayoutParams.TYPE_PHONE
}

fun getStreamText(editText: EditText): Observable<String> {
    return RxTextView.textChangeEvents(editText)
        .filter { editText.isFocused }
        .distinctUntilChanged()
        .sample(300, TimeUnit.MILLISECONDS)
        .map { it.text() }
        .map { it.toString() }
        .subscribeOn(AndroidSchedulers.mainThread())
}

fun getClicks(view: View): Observable<Any> {
    return RxView.clicks(view)
        .throttleFirst(300, TimeUnit.MILLISECONDS)
        .subscribeOn(AndroidSchedulers.mainThread())
}

fun getCheckChanges(view: Switch): Observable<Boolean> {
    return RxCompoundButton.checkedChanges(view)
        .throttleFirst(300, TimeUnit.MILLISECONDS)
        .subscribeOn(AndroidSchedulers.mainThread())
}
