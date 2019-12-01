package com.rain.sitecontrol.service

import android.accessibilityservice.AccessibilityService
import android.text.InputType
import android.util.Patterns
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.jakewharton.rxrelay2.PublishRelay
import com.rain.sitecontrol.R
import com.rain.sitecontrol.utils.Constant.DANGEROUS_DOMAINS
import dagger.android.AndroidInjection
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

private const val settingPackage = "com.android.settings"
private const val forceStopId = "com.android.settings:id/right_button"

class SiteControlService : AccessibilityService() {
    private val predictTrigger: PublishRelay<AccessibilityNodeInfo> = PublishRelay.create()
    private val disposables = CompositeDisposable()

    @Inject
    lateinit var siteControlApi: SiteControlApi
    @Inject
    lateinit var siteControlRepo: SiteControlRepo

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
        listenToPredictTrigger()
    }

    private fun listenToPredictTrigger() {
        disposables.add(predictTrigger
            .switchMapSingle { predict(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .distinctUntilChanged()
            .subscribe({
                siteControlRepo.cacheResult(it.first, it.second)
                if (it.second) {
                    Timber.d("match url: ${it.second}")
                    performGlobalAction(GLOBAL_ACTION_BACK)
                }
            }, Timber::e)
        )
    }

    private fun predict(urlBar: AccessibilityNodeInfo): Single<Pair<String, Boolean>> {
        val text = urlBar.text.toString()
        if (!Patterns.WEB_URL.matcher(text).matches()) {
            Timber.d("Not a valid url: ${urlBar.text}")
            return Single.just(text to false)
        }

        val cache = siteControlRepo.getResult(urlBar.text.toString())
        if (cache != null) {
            return Single.just(Pair(text, cache))
        }

        for (domain in DANGEROUS_DOMAINS) {
            if (urlBar.text.toString().endsWith(domain)) {
                return Single.just(Pair(text, true))
            }
        }

        return doPredict(text).map { text to it }
    }

    private fun doPredict(url: String): Single<Boolean> {
        return siteControlApi.predict(PredictRequest(url))
            .map { it.result }
            .doOnError { Timber.e(it) }
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

        val descAccessibility =
            source.findAccessibilityNodeInfosByText(getString(R.string.accessibility_service_description))
        if (descAccessibility.isNotEmpty() && event.packageName == settingPackage) {
            performGlobalAction(GLOBAL_ACTION_BACK)
            return
        }

        findUrlNode(source)?.run {
            Timber.d("findUrlNode - ${this.text}")
            predictTrigger.accept(this)
        }
    }

    private fun findUrlNode(root: AccessibilityNodeInfo?): AccessibilityNodeInfo? {
        if (root == null || isUrlNode(root)) {
            return root
        }

        for (i in 0 until root.childCount) {
            val node = findUrlNode(root.getChild(i))
            if (node != null) {
                return node
            }
        }

        return null
    }

    private fun isUrlNode(node: AccessibilityNodeInfo): Boolean {
        val text = node.text?.toString() ?: ""
        return node.inputType != InputType.TYPE_NULL && Patterns.WEB_URL.matcher(text).matches()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}
