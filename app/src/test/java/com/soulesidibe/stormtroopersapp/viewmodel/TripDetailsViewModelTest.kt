package com.soulesidibe.stormtroopersapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.soulesidibe.stormtroopersapp.ImmediateSchedulerProvider
import com.soulesidibe.stormtroopersapp.ResourceState
import com.soulesidibe.stormtroopersapp.model.TripDetailsModel
import com.soulesidibe.stormtroopersapp.model.getFakeTrip
import io.reactivex.Single
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.internal.verification.VerificationModeFactory

/**
 * Created on 11/25/18 at 1:59 AM
 * Project name : StormTroopersApp
 */
class TripDetailsViewModelTest : KoinTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `should consume trip details from model`() {
        val model = Mockito.mock(TripDetailsModel::class.java)
        val scheduler = ImmediateSchedulerProvider()
        val viewModel = TripDetailsViewModel(model, scheduler)

        `when`(model.getTripDetailsBy(1)).thenReturn(Single.just(getFakeTrip()))
        viewModel.getLastTrips(1)

        val observeLastTripsData = viewModel.observeTripData()
        val resource = observeLastTripsData.value
        Assert.assertNotNull(resource)
        Assert.assertThat(resource!!.status, `is`(ResourceState.SUCCESS))
        Assert.assertThat(resource.data, `is`(getFakeTrip()))
        Assert.assertThat(resource.data!!.id, `is`(1))
        verify(model, VerificationModeFactory.atMost(1)).getTripDetailsBy(1)
    }

    @Test
    fun `should get error from model`() {
        val model = Mockito.mock(TripDetailsModel::class.java)
        val scheduler = ImmediateSchedulerProvider()
        val viewModel = TripDetailsViewModel(model, scheduler)

        `when`(model.getTripDetailsBy(1)).thenReturn(Single.error(Throwable()))
        viewModel.getLastTrips(1)

        val observeLastTripsData = viewModel.observeTripData()
        val resource = observeLastTripsData.value
        Assert.assertNotNull(resource)
        Assert.assertThat(resource!!.status, `is`(ResourceState.ERROR))
        verify(model, VerificationModeFactory.atMost(1)).getTripDetailsBy(1)
    }
}