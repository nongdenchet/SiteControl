package com.rain.sitecontrol.service

import android.accessibilityservice.AccessibilityService
import android.os.Bundle
import android.support.annotation.MainThread
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.jakewharton.rxrelay2.PublishRelay
import com.rain.sitecontrol.R
import dagger.android.AndroidInjection
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val settingPackage = "com.android.settings"
private const val urlBarId = "com.android.chrome:id/url_bar"
private const val forceStopId = "com.android.settings:id/right_button"
private const val debounceTime = 300L

class SiteControlService : AccessibilityService() {
    private val predictTrigger: PublishRelay<AccessibilityNodeInfo> = PublishRelay.create()
    private val disposables = CompositeDisposable()

    @Inject
    lateinit var siteControlApi: SiteControlApi

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
        listenToPredictTrigger()
    }

    private fun listenToPredictTrigger() {
        disposables.add(predictTrigger
                .throttleLast(debounceTime, TimeUnit.MILLISECONDS)
                .switchMapSingle { predict(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.second) {
                        preventUser(it.first)
                    }
                }, Timber::e)
        )
    }

    private fun predict(urlBar: AccessibilityNodeInfo): Single<Pair<AccessibilityNodeInfo, Boolean>> {
        return Single.zip(Single.just(urlBar), doPredict(urlBar.text.toString()),
                BiFunction { node, result -> Pair(node, result) })
    }

    private fun doPredict(url: String): Single<Boolean> {
        return siteControlApi.predict(PredictRequest(url))
                .map { it.result }
                .onErrorReturnItem(false)
                .subscribeOn(Schedulers.io())
    }

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
            predictTrigger.accept(urlBarQuery[0])
        }
    }

    @MainThread
    private fun preventUser(urlBar: AccessibilityNodeInfo) {
        val args = Bundle()
        args.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "")
        urlBar.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, args)
        performGlobalAction(GLOBAL_ACTION_BACK)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}
