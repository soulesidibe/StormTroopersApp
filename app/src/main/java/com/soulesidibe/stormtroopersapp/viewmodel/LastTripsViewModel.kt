package com.soulesidibe.stormtroopersapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.soulesidibe.stormtroopersapp.BaseSchedulerProvider
import com.soulesidibe.stormtroopersapp.Resource
import com.soulesidibe.stormtroopersapp.ResourceState
import com.soulesidibe.stormtroopersapp.model.LastTripsModel
import com.soulesidibe.stormtroopersapp.model.Trip
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Created on 11/24/18 at 10:49 PM
 * Project name : StormTroopersApp
 */
class LastTripsViewModel @Inject constructor(
    private val model: LastTripsModel,
    private val scheduler: BaseSchedulerProvider
) : ViewModel() {

    private val mDisposable: CompositeDisposable = CompositeDisposable()

    private val lastTripsLiveData: MutableLiveData<Resource<List<Trip>>> = MutableLiveData()

    fun observeLastTripsData(): LiveData<Resource<List<Trip>>> {
        return lastTripsLiveData
    }


    fun getLastTrips() {
        lastTripsLiveData.postValue(Resource(ResourceState.LOADING))
        val disposable = model.getLastTrips()
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())
            .subscribe(
                {
                    lastTripsLiveData.postValue(Resource(ResourceState.SUCCESS, it))
                },
                {
                    lastTripsLiveData.postValue(Resource(ResourceState.ERROR))
                }
            )

        mDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        mDisposable.clear()
    }
}