package com.soulesidibe.stormtroopersapp.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 * Created on 11/24/18 at 10:56 PM
 * Project name : StormTroopersApp
 */

interface StarWarsAPI {

    @Headers("Accept: application/json; charset=UTF-8")
    @GET("/trips")
    fun getLastTrips(): Call<List<Trip>>

    @Headers("Accept: application/json; charset=UTF-8")
    @GET("/trips/{id}")
    fun getTripDetails(@Path("id") id: Int): Call<Trip>
}
