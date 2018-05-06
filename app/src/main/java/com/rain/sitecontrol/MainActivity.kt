package com.rain.sitecontrol

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.rain.auth.ui.SetupNavigator

class MainActivity : AppCompatActivity() {
    private lateinit var setupNavigator: SetupNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNavigator = SetupNavigator(this)
        findViewById<View>(R.id.hello_world).setOnClickListener {
            setupNavigator.navigate()
        }
    }
}
