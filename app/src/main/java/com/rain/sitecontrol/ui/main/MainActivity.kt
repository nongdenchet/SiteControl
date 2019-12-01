package com.rain.sitecontrol.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rain.auth.data.AuthManager
import com.rain.core.utils.getClicks
import com.rain.core.utils.subscribeMain
import com.rain.sitecontrol.R
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.btnTestAuth
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private val disposables = CompositeDisposable()

    @Inject
    internal lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        disposables.add(getClicks(btnTestAuth)
                .flatMapMaybe { authManager.requireAuthComplete(it) }
                .observeOn(Schedulers.io())
                .flatMap { Observable.just(it) }
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribeMain({
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                }, Timber::e))
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }
}