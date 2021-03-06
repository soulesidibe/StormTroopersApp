package com.soulesidibe.stormtroopersapp

import io.reactivex.SingleEmitter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat

/**
 * Created on 11/24/18 at 11:27 PM
 * Project name : StormTroopersApp
 */


fun <T> SingleEmitter<T>.notDisposedOnError(t: Throwable?) {
    if (!isDisposed) {
        if (t == null) {
            onError(NoData())
        } else {
            onError(t)
        }
    }
}

fun <T> SingleEmitter<T>.notDisposedOnSuccess(t: T?) {
    if (!isDisposed) {
        if (t == null) {
            onError(NoData())
        } else {
            onSuccess(t)
        }
    }
}

fun String.formatDistance(): String {

    val instance = NumberFormat.getInstance()
    instance.minimumFractionDigits = 0
    instance.isParseIntegerOnly = true
    return instance.format(this.toDoubleOrNull() ?: 0.0)
}

abstract class OnSuccess<T : Any>(private val emitter: SingleEmitter<T>) : Callback<T> {

    override fun onFailure(call: Call<T>?, t: Throwable?) {
        emitter.notDisposedOnError(t)
    }

    override fun onResponse(call: Call<T>?, response: Response<T>?) {
        if (response == null) {
            emitter.notDisposedOnError(NoData())
            return
        }
        handleResponse(response)
    }

    abstract fun handleResponse(response: Response<T>)
}
