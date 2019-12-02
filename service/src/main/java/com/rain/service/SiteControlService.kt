package com.rain.service

import android.accessibilityservice.AccessibilityService
import android.app.AlertDialog
import android.os.Handler
import android.text.InputType
import android.util.Patterns
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.jakewharton.rxrelay2.PublishRelay
import com.rain.core.utils.getBrowserPackages
import com.rain.core.utils.getOverlayType
import dagger.android.AndroidInjection
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val delayInMiliSeconds = 1000L
private const val settingPackage = "com.android.settings"
private const val forceStopId = "com.android.settings:id/right_button"
private val DANGEROUS_DOMAINS = setOf(".sexy", ".sex", ".porn", ".xxx", ".adult")

class SiteControlService : AccessibilityService() {
    private val predictTrigger: PublishRelay<AccessibilityNodeInfo> = PublishRelay.create()
    private val disposables = CompositeDisposable()
    private val handler = Handler()
    private var currentDialog: AlertDialog? = null

    @Inject
    internal lateinit var siteControlApi: SiteControlApi
    @Inject
    internal lateinit var siteControlRepo: SiteControlRepo

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
        listenToPredictTrigger()
    }

    private fun listenToPredictTrigger() {
        disposables.add(predictTrigger.switchMapMaybe { predict(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                siteControlRepo.cacheResult(it.first, it.second)
                if (it.second) {
                    Timber.d("match url: ${it.second}")
                    preventUser()
                }
            }, Timber::e)
        )
    }

    private fun predict(urlBar: AccessibilityNodeInfo): Maybe<Pair<String, Boolean>> {
        val text = urlBar.text.toString().replace("\u200E", "").trim()
        if (!Patterns.WEB_URL.matcher(text).matches()) {
            Timber.d("Not a valid url: ${urlBar.text}")
            return Maybe.just(text to false)
        }

        val cache = siteControlRepo.getResult(urlBar.text.toString())
        if (cache != null) {
            Timber.d("Using cache: $cache")
            return Maybe.just(Pair(text, cache))
        }

        for (domain in DANGEROUS_DOMAINS) {
            if (urlBar.text.toString().endsWith(domain)) {
                return Maybe.just(Pair(text, true))
            }
        }

        return doPredict(text).map { text to it }
    }

    private fun doPredict(url: String): Maybe<Boolean> {
        return siteControlApi.predict(PredictRequest(url))
            .retryWhen { it.take(3).delay(300, TimeUnit.MILLISECONDS) }
            .map { it.result }
            .toMaybe()
            .doOnError { Timber.e(it) }
            .onErrorComplete()
            .subscribeOn(Schedulers.io())
    }

    override fun onInterrupt() {}

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (!siteControlRepo.isEnabled()) {
            Timber.d("Not enabled")
            return
        }

        val browserPackages = getBrowserPackages(this).toSet()
        if (!browserPackages.contains(event.packageName)) {
            Timber.d("package is not browser: ${event.packageName}")
            return
        }

        val source = event.source
        if (source == null) {
            Timber.d("source is null")
            return
        }

        val title = source.findAccessibilityNodeInfosByText(getString(R.string.title))
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

    private fun preventUser() {
        if (currentDialog != null) {
            return
        }

        currentDialog = AlertDialog.Builder(applicationContext)
            .setTitle(R.string.title)
            .setMessage(R.string.adult_message)
            .setIcon(R.drawable.ic_warning)
            .setCancelable(false)
            .setPositiveButton(
                R.string.go_back
            ) { d, _ ->
                d.dismiss()
                performGlobalAction(GLOBAL_ACTION_BACK)
                performGlobalAction(GLOBAL_ACTION_BACK)
                handler.postDelayed({ currentDialog = null }, delayInMiliSeconds)
            }
            .create()
            .apply {
                window?.setType(getOverlayType())
                show()
            }
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
