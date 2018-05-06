package com.rain.auth.ui.auth.di

interface AuthDialogComponentProvider {
    fun plus(authDialogModule: AuthDialogModule): AuthDialogComponent
}
