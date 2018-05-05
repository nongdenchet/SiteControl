package com.rain.auth

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import dagger.android.AndroidInjection
import javax.inject.Inject

class SetupActivity: AppCompatActivity() {

    @Inject
    lateinit var setupViewModel: SetupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)
        findViewById<TextView>(R.id.test).text = setupViewModel.getTitle()
    }
}
