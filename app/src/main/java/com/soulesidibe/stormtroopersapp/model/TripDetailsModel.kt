package com.soulesidibe.stormtroopersapp.model

import com.soulesidibe.stormtroopersapp.NoData
import com.soulesidibe.stormtroopersapp.OnSuccess
import com.soulesidibe.stormtroopersapp.notDisposedOnError
import com.soulesidibe.stormtroopersapp.notDisposedOnSuccess
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject

/**
 * Created on 11/25/18 at 1:27 AM
 * Project name : StormTroopersApp
 */


interface TripDetailsModel {

    fun getTripDetailsBy(id: Int): Single<Trip>
}

class TripDetailsModelImpl @Inject constructor(
    private val api: StarWarsAPI,
    private val internetManager: InternetManager
) : TripDetailsModel {

    override fun getTripDetailsBy(id: Int): Single<Trip> = internetManager.whenInternetAvailable {
        val call = api.getTripDetails(id)

        val callback = object : OnSuccess<Trip>(this) {
            override fun handleResponse(response: Response<Trip>) {
                if (response.code() == 200) {
                    val body = response.body()
                    notDisposedOnSuccess(response.body())
                } else {
                    notDisposedOnError(NoData())
                }
            }
        }
        call.enqueue(callback)
    }

}
