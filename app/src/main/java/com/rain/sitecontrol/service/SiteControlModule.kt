package com.rain.sitecontrol.service

import dagger.Module
import dagger.Provides

@Module
class SiteControlModule {

    @SiteControlScope
    @Provides
    fun provideSiteControlRepo(): SiteControlRepo {
        return SiteControlRepo()
    }
}
