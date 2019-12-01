package com.rain.sitecontrol.ui.main

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
            .flatMapMaybe { value ->
                authManager.requireAuthComplete(value)
                    .doOnComplete { resetValue() }
                    .map { value }
            }
            .doOnSubscribe { resetValue() }
            .subscribeMain({
                siteControlRepo.setEnabled(it)
                ivStatus.setImageDrawable(getStatusIcon(it))
                sEnabled.isChecked = it
            }, Timber::e)
        )
    }

    private fun resetValue() {
        siteControlRepo.isEnabled()
            .run {
                ivStatus.setImageDrawable(getStatusIcon(this))
                sEnabled.isChecked = this
            }
    }

    private fun getStatusIcon(status: Boolean): Drawable? {
        val icon = if (status) R.drawable.ic_protected else R.drawable.ic_not_protected

        return ContextCompat.getDrawable(this, icon)
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }
}
