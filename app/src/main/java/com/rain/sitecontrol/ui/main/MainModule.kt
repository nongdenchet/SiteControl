package com.rain.sitecontrol.ui.main

import android.content.SharedPreferences
import com.rain.service.SiteControlRepo
import dagger.Module
import dagger.Provides

@Module
class MainModule {

    @MainScope
    @Provides
    fun provideSiteControlRepo(sharedPreferences: SharedPreferences): SiteControlRepo {
        return SiteControlRepo(sharedPreferences)
    }
}
