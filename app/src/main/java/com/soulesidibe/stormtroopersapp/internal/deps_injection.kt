package com.soulesidibe.stormtroopersapp.internal

import com.soulesidibe.stormtroopersapp.BuildConfig
import com.soulesidibe.stormtroopersapp.model.*
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created on 11/24/18 at 10:51 PM
 * Project name : StormTroopersApp
 */

val appModule = module {

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_SERVER)
            .addConverterFactory(MoshiConverterFactory.create())
            .build() as Retrofit
    }
    single { get<Retrofit>().create(StarWarsAPI::class.java) }

    single<InternetManager> { AndroidInternetManager(androidApplication()) }
    single<LastTripsModel> { LastTripsModelImpl(get(), get()) }
}