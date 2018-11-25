package com.soulesidibe.stormtroopersapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.soulesidibe.stormtroopersapp.ImmediateSchedulerProvider
import com.soulesidibe.stormtroopersapp.NoData
import com.soulesidibe.stormtroopersapp.ResourceState
import com.soulesidibe.stormtroopersapp.model.LastTripsModel
import com.soulesidibe.stormtroopersapp.model.getFakeTrips
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
class LastTripsViewModelTest : KoinTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `should consume last trips from model`() {
        val model = Mockito.mock(LastTripsModel::class.java)
        val scheduler = ImmediateSchedulerProvider()
        val viewModel = LastTripsViewModel(model, scheduler)

        `when`(model.getLastTrips()).thenReturn(Single.just(getFakeTrips()))
        viewModel.getLastTrips()

        val observeLastTripsData = viewModel.observeLastTripsData()
        val resource = observeLastTripsData.value
        Assert.assertNotNull(resource)
        Assert.assertThat(resource!!.status, `is`(ResourceState.SUCCESS))
        Assert.assertThat(resource.data, `is`(getFakeTrips()))
        Assert.assertThat(resource.data!![0].id, `is`(1))
        verify(model, VerificationModeFactory.atMost(1)).getLastTrips()
    }

    @Test
    fun `should get an error from model`() {
        val model = Mockito.mock(LastTripsModel::class.java)
        val scheduler = ImmediateSchedulerProvider()
        val viewModel = LastTripsViewModel(model, scheduler)

        `when`(model.getLastTrips()).thenReturn(Single.error(NoData()))
        viewModel.getLastTrips()

        val observeLastTripsData = viewModel.observeLastTripsData()
        val resource = observeLastTripsData.value
        Assert.assertNotNull(resource)
        Assert.assertThat(resource!!.status, `is`(ResourceState.ERROR))
        verify(model, VerificationModeFactory.atMost(1)).getLastTrips()
    }
}