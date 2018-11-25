package com.soulesidibe.stormtroopersapp.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.soulesidibe.stormtroopersapp.*
import com.soulesidibe.stormtroopersapp.model.Trip
import com.soulesidibe.stormtroopersapp.viewmodel.TripDetailsViewModel
import com.squareup.picasso.Picasso
import kotterknife.bindView
import kotterknife.bindViews
import org.joda.time.DateTime
import org.koin.android.ext.android.inject
import kotlin.math.roundToInt

class TripDetailsActivity : AppCompatActivity() {

    private val TAG = "TripDetailsActivity"

    private val toolbar by bindView<Toolbar>(R.id.toolbar)
    private val pilotAvatar by bindView<ImageView>(R.id.id_activity_Trip_detail_imageview)
    private val pilotName by bindView<TextView>(R.id.id_activity_Trip_detail_pilot_name)
    private val departurePlace by bindView<TextView>(R.id.id_activity_Trip_detail_departure)
    private val arrivalPlace by bindView<TextView>(R.id.id_activity_Trip_detail_arrival)
    private val pickUpTime by bindView<TextView>(R.id.id_activity_Trip_detail_pickup_time)
    private val dropOffTime by bindView<TextView>(R.id.id_activity_Trip_detail_dropoff_time)
    private val tripDistance by bindView<TextView>(R.id.id_activity_Trip_detail_distance)
    private val tripDuration by bindView<TextView>(R.id.id_activity_Trip_detail_duration)
    private val noRate by bindView<TextView>(R.id.id_activity_Trip_detail_no_rate)
    private val groupStars by bindView<Group>(R.id.id_activity_Trip_detail_stars_group)
    private val stars by bindViews<ImageView>(
        R.id.id_activity_Trip_detail_imageView2,
        R.id.id_activity_Trip_detail_imageView3,
        R.id.id_activity_Trip_detail_imageView4,
        R.id.id_activity_Trip_detail_imageView5,
        R.id.id_activity_Trip_detail_imageView6
    )

    private fun showTripDetails(trip: Trip) {
        Picasso.get().load(BuildConfig.API_SERVER + trip.pilot.avatar).into(pilotAvatar)
        pilotName.text = trip.pilot.name.toUpperCase()
        departurePlace.text = trip.pick_up.name.toUpperCase()
        pickUpTime.text = DateTime.parse(trip.pick_up.date).run {
            "$hourOfDay:$minuteOfHour"
        }
        arrivalPlace.text = trip.drop_off.name.toUpperCase()
        dropOffTime.text = DateTime.parse(trip.drop_off.date).run {
            "$hourOfDay:$minuteOfHour"
        }
        tripDistance.text = formattedDistance(trip)

        tripDuration.text = getDuration(trip.duration)

        setupRating(trip.pilot.rating.roundToInt())
    }

    private fun setupRating(rate: Int) {
        if (rate == 0) {
            groupStars.visibility = View.GONE
            noRate.visibility = View.VISIBLE
        } else {
            stars.take(rate).forEach {
                it.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_filled))
            }
            stars.takeLast(5 - rate).forEach {
                it.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_empty))
            }
            groupStars.visibility = View.VISIBLE
            noRate.visibility = View.GONE
        }
    }

    private fun getDuration(duration: Int): String {
        val seconds = (duration / 1000) % 60
        val minutes = ((duration / (1000 * 60)) % 60)
        val hours = ((duration / (1000 * 60 * 60)) % 24)

        return "$hours:$minutes:$seconds"
    }

    private fun formattedDistance(trip: Trip) =
        "${trip.distance.value.toString().formatDistance()} ${trip.distance.unit.toUpperCase()}"


    private val tripDetailsViewModel: TripDetailsViewModel by inject()

    private val tripId: Int by lazy { intent.getIntExtra(INTENT_EXTRA_TRIP_ID, 0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_details)
        setSupportActionBar(toolbar)

        supportActionBar?.also {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowTitleEnabled(false)
            it.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
        }

        tripDetailsViewModel.observeTripData().observe(this, observeTripDetails)
        tripDetailsViewModel.getLastTrips(tripId)
    }

    private val observeTripDetails = Observer<Resource<Trip>> {
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
                    showTripDetails(trips)
                }
            }

            ResourceState.ERROR -> {
                showEmpty()
            }
        }
    }


    private fun showEmpty() {

    }

    private fun showLoading() {

    }


    companion object {

        const val INTENT_EXTRA_TRIP_ID = "extra_trip_id"

        fun getIntent(context: Context, tripId: Int): Intent {
            val intent = Intent(context, TripDetailsActivity::class.java)
            intent.putExtra(INTENT_EXTRA_TRIP_ID, tripId)
            return intent
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

}
