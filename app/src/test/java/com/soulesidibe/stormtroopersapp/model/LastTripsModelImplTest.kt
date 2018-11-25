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
class LastTripsModelImplTest {


    @Test
    fun `should return internet error when no internet available`() {
        val api = Mockito.mock(StarWarsAPI::class.java)
        val internetManager = Mockito.mock(InternetManager::class.java)
        `when`(internetManager.hasInternet()).thenReturn(false)


        val model = LastTripsModelImpl(api, internetManager)
        val test = model.getLastTrips().test()
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
        val noTrips: List<Trip>? = null
        `when`(api.getLastTrips()).thenReturn(Calls.response(noTrips))


        val model = LastTripsModelImpl(api, internetManager)
        val test = model.getLastTrips().test()
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
        val noTrips: List<Trip>? = null
        `when`(api.getLastTrips()).thenReturn(Calls.failure(NoData()))


        val model = LastTripsModelImpl(api, internetManager)
        val test = model.getLastTrips().test()
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
        `when`(api.getLastTrips()).thenReturn(Calls.response(getFakeTrips()))

        val model = LastTripsModelImpl(api, internetManager)
        val test = model.getLastTrips().test()

        test.isTerminated
        test.assertValue(getFakeTrips())
    }

}

fun getFakeTrips(): List<Trip> {
    return listOf(
        Trip(
            1,
            Pilot("Dark Vador", "/static/dark-vador.png", 5),
            Distance(2478572, "km"),
            19427000,
            PlaceInfos("Yavin 4", "/static/yavin-4.png", "2017-12-09T14:12:51Z"),
            PlaceInfos("Naboo", "/static/naboo.png", "2017-12-09T19:35:51Z")
        ),
        Trip(
            2,
            Pilot("Admiral Ackbar", "/static/admiral-ackbar.png", 0),
            Distance(24785727853, "km"),
            19427000,
            PlaceInfos("Naboo", "/static/naboo.png", "2017-12-09T14:12:51Z"),
            PlaceInfos("Coruscant", "/static/coruscant.png", "2017-12-09T19:35:51Z")
        )
    )
}
