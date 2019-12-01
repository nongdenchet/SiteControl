package com.rain.service

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class SiteControlModule {

    @SiteControlScope
    @Provides
    fun provideSiteControlApi(retrofit: Retrofit): SiteControlApi {
        return retrofit.create(SiteControlApi::class.java)
    }

    @SiteControlScope
    @Provides
    fun provideSiteControlRepo(sharedPreferences: SharedPreferences): SiteControlRepo {
        return SiteControlRepo(sharedPreferences)
    }
}
