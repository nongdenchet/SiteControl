package com.rain.sitecontrol.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rain.auth.data.AuthManager
import com.rain.core.utils.getCheckChanges
import com.rain.core.utils.subscribeMain
import com.rain.service.SiteControlRepo
import com.rain.sitecontrol.R
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private val disposables = CompositeDisposable()

    @Inject
    internal lateinit var authManager: AuthManager
    @Inject
    internal lateinit var siteControlRepo: SiteControlRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialize()
    }

    private fun initialize() {
        disposables.add(getCheckChanges(sEnabled)
            .filter { it != siteControlRepo.isEnabled() }
            .doOnSubscribe { resetValue() }
            .flatMapMaybe { value ->
                authManager.requireAuthComplete(value)
                    .doOnComplete { resetValue() }
                    .map { value }
            }
            .subscribeMain({
                siteControlRepo.setEnabled(it)
                sEnabled.isChecked = it
            }, Timber::e)
        )
    }

    private fun resetValue() {
        sEnabled.isChecked = siteControlRepo.isEnabled()
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }
}
