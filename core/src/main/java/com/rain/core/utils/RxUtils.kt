package com.rain.core.utils

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

fun <T> Observable<T>.subscribeMain(action: (T) -> Unit, error: (Throwable) -> Unit): Disposable {
    return observeOn(AndroidSchedulers.mainThread())
            .subscribe({ action.invoke(it) }, { error.invoke(it) })
}
