package com.rain.sitecontrol.di.application

import android.content.Context
import com.rain.auth.ui.setup.SetupNavigator
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NavigatorModule {

    @Singleton
    @Provides
    fun provideSetupNavigator(context: Context): SetupNavigator {
        return SetupNavigator(context)
    }
}
