package com.rain.auth.ui.auth.di

import com.rain.auth.ui.auth.AuthDialog
import dagger.Subcomponent

@AuthDialogScope
@Subcomponent(modules = [(AuthDialogModule::class)])
interface AuthDialogComponent {
    fun inject(authDialog: AuthDialog)
}
