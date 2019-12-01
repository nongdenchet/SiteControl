package com.rain.auth.data

import android.content.Context
import android.os.Looper
import android.widget.Toast
import com.rain.auth.ui.auth.AuthDialog
import io.reactivex.Maybe

class AuthManager(private val authRepo: AuthRepo, private val context: Context) {

    class NotSetupPassword : Throwable()

    fun hasSetup() = authRepo.hasSetup()

    fun <T> requireAuthComplete(value: T): Maybe<T> {
        return requireAuth(value)
                .onErrorComplete {
                    if (it is AuthManager.NotSetupPassword) {
                        Toast.makeText(context, "Please setup pin", Toast.LENGTH_SHORT).show()
                    }
                    return@onErrorComplete true
                }
    }

    private fun <T> requireAuth(value: T): Maybe<T> {
        return Maybe.create<T> {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                throw IllegalStateException("Should observe on main")
            }

            if (!authRepo.hasSetup()) {
                it.onError(NotSetupPassword())
                return@create
            }

            val authDialog = AuthDialog(context)
            authDialog.listener = object : AuthDialog.Listener {
                override fun onDismiss() {
                    it.onComplete()
                }

                override fun onSuccess() {
                    it.onSuccess(value)
                }
            }
            authDialog.show()
        }
    }
}
