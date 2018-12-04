package com.soulesidibe.stormtroopersapp.model

import android.content.Context
import android.net.ConnectivityManager
import com.soulesidibe.stormtroopersapp.NoInternetException
import com.soulesidibe.stormtroopersapp.notDisposedOnError
import io.reactivex.Single
import io.reactivex.SingleEmitter
import javax.inject.Inject

/**
 * Created on 11/24/18 at 11:22 PM
 * Project name : StormTroopersApp
 */


interface InternetManager {
    fun hasInternet(): Boolean
}

fun <T> InternetManager.whenInternetAvailable(func: SingleEmitter<T>.() -> Unit): Single<T> {
    return Single.create { emitter ->
        if (!hasInternet()) {
            emitter.notDisposedOnError(NoInternetException())
        } else {
            emitter.func()
        }
    }
}


class AndroidInternetManager @Inject constructor(private val context: Context) : InternetManager {

    override fun hasInternet(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}
