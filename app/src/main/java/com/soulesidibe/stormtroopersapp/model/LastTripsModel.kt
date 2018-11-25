package com.soulesidibe.stormtroopersapp.model

import com.soulesidibe.stormtroopersapp.NoData
import com.soulesidibe.stormtroopersapp.OnSuccess
import com.soulesidibe.stormtroopersapp.notDisposedOnError
import com.soulesidibe.stormtroopersapp.notDisposedOnSuccess
import io.reactivex.Single
import retrofit2.Response

/**
 * Created on 11/24/18 at 10:49 PM
 * Project name : StormTroopersApp
 */

interface LastTripsModel {

    fun getLastTrips(): Single<List<Trip>>

    fun getTripDetails(id: Int): Single<Trip>
}

class LastTripsModelImpl(
    private val api: StarWarsAPI,
    private val internetManager: InternetManager
) : LastTripsModel {

    override fun getLastTrips(): Single<List<Trip>> = internetManager.whenInternetAvailable {
        val call = api.getLastTrips()

        val callback = object : OnSuccess<List<Trip>>(it) {
            override fun handleResponse(response: Response<List<Trip>>) {
                if (response.code() == 200) {
                    val body = response.body()
                    it.notDisposedOnSuccess(response.body())
                } else {
                    it.notDisposedOnError(NoData())
                }
            }

        }
        call.enqueue(callback)
    }

    override fun getTripDetails(id: Int): Single<Trip> = internetManager.whenInternetAvailable {
        val call = api.getTripDetails(id)

        val callback = object : OnSuccess<Trip>(it) {
            override fun handleResponse(response: Response<Trip>) {
                if (response.code() == 200) {
                    val body = response.body()
                    it.notDisposedOnSuccess(response.body())
                } else {
                    it.notDisposedOnError(NoData())
                }
            }
        }
        call.enqueue(callback)

    }

}