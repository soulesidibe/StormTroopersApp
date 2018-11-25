package com.soulesidibe.stormtroopersapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.soulesidibe.stormtroopersapp.BuildConfig
import com.soulesidibe.stormtroopersapp.R
import com.soulesidibe.stormtroopersapp.model.Trip
import com.squareup.picasso.Picasso
import kotlin.math.roundToInt

/**
 * Created on 11/25/18 at 11:50 AM
 * Project name : StormTroopersApp
 */
class LastTripsAdapter(
    private var trips: MutableList<Trip>,
    private val onClickListener: TripAdapterOnClick
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface TripAdapterOnClick {
        fun onTripClicked(trip: Trip)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        if (viewType == WITHOUT_RATING) {
            return LastTripsViewHolder(inflater.inflate(R.layout.adapter_trip_item, parent, false))
        } else {
            return LastTripsViewHolderRating(inflater.inflate(R.layout.adapter_trip_item_rating, parent, false))
        }
    }

    override fun getItemCount() = trips.size

    override fun getItemViewType(position: Int) = if (trips[position].pilot.rating.toInt() > 0) {
        WITH_RATING
    } else {
        WITHOUT_RATING
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == WITHOUT_RATING) {
            (holder as LastTripsViewHolder).bind()
        } else {
            (holder as LastTripsViewHolderRating).bind()
        }
    }

    fun setTrips(newTrips: List<Trip>) {
        trips.clear()
        trips.addAll(newTrips)
        notifyDataSetChanged()
    }

    inner class LastTripsViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private val pilotPhoto: ImageView = view.findViewById(R.id.id_item_trip_imageview)
        private val pilotName: TextView = view.findViewById(R.id.id_item_trip_pilot_name)
        private val pickUp: TextView = view.findViewById(R.id.id_item_trip_pickup)
        private val dropOff: TextView = view.findViewById(R.id.id_item_trip_dropoff)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onClickListener.onTripClicked(trips[adapterPosition])
        }

        fun bind() {
            val trip = trips[adapterPosition]
            Picasso.get().load(BuildConfig.API_SERVER + trip.pilot.avatar).into(pilotPhoto)
            pilotName.text = trip.pilot.name
            pickUp.text = trip.pick_up.name
            dropOff.text = trip.drop_off.name
        }
    }

    inner class LastTripsViewHolderRating(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private val pilotPhoto: ImageView = view.findViewById(R.id.id_item_trip_imageview)
        private val pilotName: TextView = view.findViewById(R.id.id_item_trip_pilot_name)
        private val pickUp: TextView = view.findViewById(R.id.id_item_trip_pickup)
        private val dropOff: TextView = view.findViewById(R.id.id_item_trip_dropoff)
        private val stars: List<ImageView> = listOf(
            view.findViewById(R.id.id_item_trip_imageView2),
            view.findViewById(R.id.id_item_trip_imageView3),
            view.findViewById(R.id.id_item_trip_imageView4),
            view.findViewById(R.id.id_item_trip_imageView5),
            view.findViewById(R.id.id_item_trip_imageView6)
        )

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onClickListener.onTripClicked(trips[adapterPosition])
        }

        fun bind() {
            val trip = trips[adapterPosition]
            Picasso.get().load(BuildConfig.API_SERVER + trip.pilot.avatar).into(pilotPhoto)
            pilotName.text = trip.pilot.name
            pickUp.text = trip.pick_up.name
            dropOff.text = trip.drop_off.name
            val rate = trip.pilot.rating.roundToInt()
            stars.take(rate).forEach {
                it.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_star_filled))
            }
            stars.takeLast(5 - rate).forEach {
                it.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_star_empty))
            }
        }

    }

    companion object {
        const val WITHOUT_RATING = 100
        const val WITH_RATING = 200
    }
}