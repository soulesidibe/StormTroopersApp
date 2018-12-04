package com.soulesidibe.stormtroopersapp.view.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.soulesidibe.stormtroopersapp.R
import com.soulesidibe.stormtroopersapp.Resource
import com.soulesidibe.stormtroopersapp.ResourceState
import com.soulesidibe.stormtroopersapp.internal.ContextModule
import com.soulesidibe.stormtroopersapp.internal.DaggerLastTripComponent
import com.soulesidibe.stormtroopersapp.model.Trip
import com.soulesidibe.stormtroopersapp.view.TripsDividerItemDecoration
import com.soulesidibe.stormtroopersapp.view.adapter.LastTripsAdapter
import com.soulesidibe.stormtroopersapp.viewmodel.LastTripsViewModel
import kotterknife.bindView
import javax.inject.Inject

class LastTripsActivity : AppCompatActivity() {

    private val TAG = "LastTripsActivity"

    private val toolbar by bindView<Toolbar>(R.id.toolbar)
    private val recyclerView by bindView<RecyclerView>(R.id.id_activity_lastTrips_recyclerview)
    private val loadingView by bindView<ConstraintLayout>(R.id.id_activity_lastTrips_loading_layout)
    private val emptyView by bindView<ConstraintLayout>(R.id.id_activity_operations_empty_layout)
    private lateinit var adapter: LastTripsAdapter


    @Inject
    lateinit var lastTripsViewModel: LastTripsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerLastTripComponent.builder()
            .contextModule(ContextModule(applicationContext))
            .build().inject(this)

        setContentView(R.layout.activity_last_trips)
        setSupportActionBar(toolbar)
        setTitle(R.string.str_activity_last_trips_title)

        initRecyclerView()
        lastTripsViewModel.observeLastTripsData().observe(this, lastTripsObserver)
        lastTripsViewModel.getLastTrips()
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)
        val divider = ContextCompat.getDrawable(this, R.drawable.ic_devider_center_alone)
        if (divider != null) {
            recyclerView.addItemDecoration(TripsDividerItemDecoration(divider))
        }

        adapter = LastTripsAdapter(mutableListOf()) {
            onTripClicked(this)
        }
        recyclerView.adapter = adapter
    }

    private fun onTripClicked(trip: Trip) {
        val intent = TripDetailsActivity.getIntent(this, trip.id)
        startActivity(intent)
    }


    private val lastTripsObserver = Observer<Resource<List<Trip>>> {
        val status = it.status
        when (status) {
            ResourceState.LOADING -> {
                showLoading()
            }

            ResourceState.SUCCESS -> {
                val trips = it.data
                if (trips == null) {
                    showEmpty()
                } else {
                    showTrips(trips)
                }
            }

            ResourceState.ERROR -> {
                showEmpty()
            }
        }
    }

    private fun showEmpty() {
        recyclerView.visibility = View.GONE
        loadingView.visibility = View.GONE
        emptyView.visibility = View.VISIBLE
    }

    private fun showTrips(trips: List<Trip>) {
        recyclerView.visibility = View.VISIBLE
        emptyView.visibility = View.GONE
        loadingView.visibility = View.GONE

        adapter.setTrips(trips)
    }

    private fun showLoading() {
        recyclerView.visibility = View.GONE
        emptyView.visibility = View.GONE
        loadingView.visibility = View.VISIBLE
    }
}
