package com.soulesidibe.stormtroopersapp.model

import com.soulesidibe.stormtroopersapp.NoData
import com.soulesidibe.stormtroopersapp.NoInternetException
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import retrofit2.mock.Calls

/**
 * Created on 11/25/18 at 12:08 AM
 * Project name : StormTroopersApp
 */
class TripDetailsModelImplTest {


    @Test
    fun `should return internet error when no internet available`() {
        val api = Mockito.mock(StarWarsAPI::class.java)
        val internetManager = Mockito.mock(InternetManager::class.java)
        `when`(internetManager.hasInternet()).thenReturn(false)


        val model = TripDetailsModelImpl(api, internetManager)
        val test = model.getTripDetailsBy(1).test()
        test.isTerminated
        test.assertOf {
            it.assertError(NoInternetException::class.java)
        }
    }

    @Test
    fun `should return no data when server return 200 but no data available`() {
        val api = Mockito.mock(StarWarsAPI::class.java)
        val internetManager = Mockito.mock(InternetManager::class.java)
        `when`(internetManager.hasInternet()).thenReturn(true)
        val trip: Trip? = null
        `when`(api.getTripDetails(1)).thenReturn(Calls.response(trip))


        val model = TripDetailsModelImpl(api, internetManager)
        val test = model.getTripDetailsBy(1).test()
        test.isTerminated
        test.assertOf {
            it.assertError(NoData::class.java)
        }
    }

    @Test
    fun `should return no data when response is not 200`() {
        val api = Mockito.mock(StarWarsAPI::class.java)
        val internetManager = Mockito.mock(InternetManager::class.java)
        `when`(internetManager.hasInternet()).thenReturn(true)
        `when`(api.getTripDetails(1)).thenReturn(Calls.failure(NoData()))


        val model = TripDetailsModelImpl(api, internetManager)
        val test = model.getTripDetailsBy(1).test()
        test.isTerminated
        test.assertOf {
            it.assertError(NoData::class.java)
        }
    }

    @Test
    fun `should return the list of trips`() {
        val api = Mockito.mock(StarWarsAPI::class.java)
        val internetManager = Mockito.mock(InternetManager::class.java)
        `when`(internetManager.hasInternet()).thenReturn(true)
        `when`(api.getTripDetails(1)).thenReturn(Calls.response(getFakeTrip()))

        val model = TripDetailsModelImpl(api, internetManager)
        val test = model.getTripDetailsBy(1).test()

        test.isTerminated
        test.assertValue(getFakeTrip())
    }

}

fun getFakeTrip() = Trip(
    1,
    Pilot("Dark Vador", "/static/dark-vador.png", 5),
    Distance(2478572, "km"),
    19427000,
    PlaceInfos("Yavin 4", "/static/yavin-4.png", "2017-12-09T14:12:51Z"),
    PlaceInfos("Naboo", "/static/naboo.png", "2017-12-09T19:35:51Z")
)
