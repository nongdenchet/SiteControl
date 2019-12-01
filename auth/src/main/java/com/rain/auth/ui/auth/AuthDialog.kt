package com.rain.auth.ui.auth

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.chaos.view.PinView
import com.rain.auth.R
import com.rain.auth.ui.auth.di.AuthDialogComponentProvider
import com.rain.auth.ui.auth.di.AuthDialogModule
import com.rain.core.utils.EMPTY_STRING
import com.rain.core.utils.getOverlayType
import com.rain.core.utils.getStreamText
import com.rain.core.utils.subscribeMain
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

class AuthDialog(private val context: Context) {
    private lateinit var tvTitle: TextView
    private lateinit var pinView: PinView

    private val disposables = CompositeDisposable()
    private var alertDialog: AlertDialog? = null
    var listener: Listener? = null

    @Inject
    lateinit var viewModel: AuthViewModel

    interface Listener {
        fun onSuccess()
        fun onDismiss()
    }

    init {
        val app = context.applicationContext
        if (app is AuthDialogComponentProvider) {
            app.plus(AuthDialogModule()).inject(this)
        } else {
            throw IllegalStateException("application context should implement AuthDialogComponentProvider")
        }
    }

    fun show() {
        if (alertDialog == null) {
            val view = createView()
            val dialog = AlertDialog.Builder(context)
                .setView(view)
                .create()
            bindViews(view)
            dialog.setOnDismissListener { dismiss() }
            dialog.window?.setType(getOverlayType())
            this.alertDialog = dialog
        }
        bindViewModel()
        alertDialog?.show()
    }

    private fun bindViews(view: View) {
        tvTitle = view.findViewById(R.id.tvTitle)
        pinView = view.findViewById(R.id.pinView)
    }

    private fun bindViewModel() {
        val output = viewModel.bind(AuthViewModel.Input(getStreamText(pinView)))
        disposables.addAll(
            output.error.subscribeMain({ bindError(it) }, Timber::e),
            output.success.subscribeMain({ bindSuccess(it) }, Timber::e)
        )
    }

    private fun bindSuccess(success: Boolean) {
        if (success) {
            pinView.setText(EMPTY_STRING)
            listener?.onSuccess()
            dismiss()
        }
    }

    private fun bindError(error: String) {
        if (error.isNotBlank()) {
            pinView.setText(EMPTY_STRING)
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("InflateParams")
    private fun createView() = LayoutInflater.from(context).inflate(R.layout.dialog_auth, null)

    private fun dismiss() {
        alertDialog?.dismiss()
        disposables.clear()
        viewModel.unbind()
        listener?.onDismiss()
    }
}
