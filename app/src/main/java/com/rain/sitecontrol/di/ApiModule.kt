package com.rain.sitecontrol.di

import com.rain.sitecontrol.service.SiteControlApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    fun provideSiteControlApi(retrofit: Retrofit): SiteControlApi {
        return retrofit.create(SiteControlApi::class.java)
    }
}
