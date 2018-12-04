package com.soulesidibe.stormtroopersapp.model

import com.soulesidibe.stormtroopersapp.NoData
import com.soulesidibe.stormtroopersapp.OnSuccess
import com.soulesidibe.stormtroopersapp.notDisposedOnError
import com.soulesidibe.stormtroopersapp.notDisposedOnSuccess
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject

/**
 * Created on 11/24/18 at 10:49 PM
 * Project name : StormTroopersApp
 */

interface LastTripsModel {

    fun getLastTrips(): Single<List<Trip>>
}

class LastTripsModelImpl @Inject constructor(
    private val api: StarWarsAPI,
    private val internetManager: InternetManager
) : LastTripsModel {

    override fun getLastTrips(): Single<List<Trip>> = internetManager.whenInternetAvailable {
        val call = api.getLastTrips()

        val callback = object : OnSuccess<List<Trip>>(this) {
            override fun handleResponse(response: Response<List<Trip>>) {
                if (response.code() == 200) {
                    val body = response.body()
                    notDisposedOnSuccess(body)
                } else {
                    notDisposedOnError(NoData())
                }
            }

        }
        call.enqueue(callback)
    }

}