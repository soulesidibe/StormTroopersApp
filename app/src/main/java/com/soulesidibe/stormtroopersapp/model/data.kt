package com.soulesidibe.stormtroopersapp.model

/**
 * Created on 11/24/18 at 10:58 PM
 * Project name : StormTroopersApp
 */


data class Trip(
    val id: Int,
    val pilot: Pilot,
    val distance: Distance,
    val duration: Int,
    val pick_up: PlaceInfos,
    val drop_off: PlaceInfos
)

data class Pilot(val name: String, val avatar: String, val rating: Int)

data class Distance(val value: Long, val unit: String)

data class PlaceInfos(val name: String, val picture: String, val date: String)