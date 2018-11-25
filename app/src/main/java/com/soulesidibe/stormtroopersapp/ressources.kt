package com.soulesidibe.stormtroopersapp

/**
 * Created on 3/23/18 at 3:38 PM
 * Project name : tamweelandroid
 */


open class Resource<out T> constructor(
    val status: ResourceState = ResourceState.LOADING,
    val data: T? = null,
    val message: String? = null
)

enum class ResourceState {
    SUCCESS, ERROR, LOADING
}