package com.soulesidibe.stormtroopersapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.soulesidibe.stormtroopersapp.BuildConfig
import com.soulesidibe.stormtroopersapp.R
import com.soulesidibe.stormtroopersapp.model.Trip
import com.squareup.picasso.Picasso

/**
 * Created on 11/25/18 at 11:50 AM
 * Project name : StormTroopersApp
 */
class LastTripsAdapter(
    private var trips: MutableList<Trip>,
    private val onClickListener: (Trip) -> Unit
) : RecyclerView.Adapter<LastTripsAdapter.LastTripsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastTripsViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.adapter_trip_item, parent, false)
        return LastTripsViewHolder(view)
    }

    override fun getItemCount() = trips.size

    override fun onBindViewHolder(holder: LastTripsViewHolder, position: Int) {
        holder.bind()
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
            onClickListener(trips[adapterPosition])
        }

        fun bind() {
            val trip = trips[adapterPosition]
            pilotName.text = trip.pilot.name
            pickUp.text = trip.pick_up.name
            dropOff.text = trip.drop_off.name
            Picasso.get().load(BuildConfig.API_SERVER + trip.pilot.avatar).into(pilotPhoto)
        }

    }
}