package com.rain.auth.ui.setup

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rain.auth.R
import com.rain.auth.ui.setup.reducer.SetupState
import com.rain.core.utils.EMPTY_STRING
import com.rain.core.utils.getClicks
import com.rain.core.utils.getStreamText
import com.rain.core.utils.subscribeMain
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_setup.*
import timber.log.Timber
import javax.inject.Inject

class SetupActivity : AppCompatActivity() {
    private val disposables = CompositeDisposable()

    companion object {
        const val REQUEST = 1002
    }

    @Inject
    lateinit var viewModel: SetupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)
        bindViewModel()
    }

    private fun bindViewModel() {
        val output = viewModel.bind(SetupViewModel.Input(
                getStreamText(pinView),
                getStreamText(pinViewConfirm),
                getClicks(btnBack)
        ))

        disposables.addAll(
                output.canGoBack.subscribeMain({ bindCanGoBack(it) }, Timber::e),
                output.title.subscribeMain({ bindTitle(it) }, Timber::e),
                output.error.subscribeMain({ bindError(it) }, Timber::e),
                output.step.subscribeMain({ bindStep(it) }, Timber::e)
        )
    }

    private fun bindStep(step: SetupState.Step) {
        when (step) {
            SetupState.Step.Confirmation -> {
                pinViewConfirm.setText(EMPTY_STRING)
                pinViewConfirm.visibility = View.VISIBLE
                pinViewConfirm.requestFocus()
                pinView.visibility = View.GONE
            }
            SetupState.Step.Init -> {
                pinView.setText(EMPTY_STRING)
                pinView.visibility = View.VISIBLE
                pinView.requestFocus()
                pinViewConfirm.visibility = View.GONE
            }
            SetupState.Step.Success -> {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    private fun bindError(error: String) {
        if (error.isNotBlank()) {
            pinViewConfirm.setText(EMPTY_STRING)
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun bindTitle(title: Int) = tvTitle.setText(title)

    private fun bindCanGoBack(canGoBack: Boolean) {
        btnBack.visibility = if (canGoBack) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        disposables.dispose()
        viewModel.unbind()
        super.onDestroy()
    }
}
