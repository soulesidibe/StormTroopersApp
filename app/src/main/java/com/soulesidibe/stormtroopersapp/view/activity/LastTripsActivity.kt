package com.soulesidibe.stormtroopersapp.view.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.soulesidibe.stormtroopersapp.R
import com.soulesidibe.stormtroopersapp.Resource
import com.soulesidibe.stormtroopersapp.ResourceState
import com.soulesidibe.stormtroopersapp.model.Trip
import com.soulesidibe.stormtroopersapp.viewmodel.LastTripsViewModel
import org.koin.android.ext.android.inject

class LastTripsActivity : AppCompatActivity() {

    private val TAG = "LastTripsActivity"

    private val lastTripsViewModel: LastTripsViewModel by inject()

    private val lastTripsObserver = Observer<Resource<List<Trip>>> {
        val status = it.status
        when (status) {
            ResourceState.LOADING -> {
                Log.d(TAG, "Loading")
            }

            ResourceState.SUCCESS -> {
                val trips = it.data
                trips?.let {
                    it.forEach { trip ->
                        Log.d(TAG, trip.toString())
                    }
                }
            }

            ResourceState.ERROR -> {
                Log.d(TAG, "Error")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_last_trips)

        lastTripsViewModel.observeLastTripsData().observe(this, lastTripsObserver)
        lastTripsViewModel.getLastTrips()
    }
}
