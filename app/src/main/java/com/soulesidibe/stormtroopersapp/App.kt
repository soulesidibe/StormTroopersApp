package com.soulesidibe.stormtroopersapp

import android.app.Application
import com.soulesidibe.stormtroopersapp.internal.appModule
import org.koin.android.ext.android.startKoin

/**
 * Created on 11/24/18 at 10:53 PM
 * Project name : StormTroopersApp
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(appModule))
    }
}