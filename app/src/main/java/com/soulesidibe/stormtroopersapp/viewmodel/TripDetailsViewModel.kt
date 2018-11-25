package com.soulesidibe.stormtroopersapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.soulesidibe.stormtroopersapp.BaseSchedulerProvider
import com.soulesidibe.stormtroopersapp.Resource
import com.soulesidibe.stormtroopersapp.ResourceState
import com.soulesidibe.stormtroopersapp.model.Trip
import com.soulesidibe.stormtroopersapp.model.TripDetailsModel
import io.reactivex.disposables.CompositeDisposable

/**
 * Created on 11/24/18 at 10:49 PM
 * Project name : StormTroopersApp
 */
class TripDetailsViewModel(
    private val model: TripDetailsModel,
    private val scheduler: BaseSchedulerProvider
) : ViewModel() {

    private val mDisposable: CompositeDisposable = CompositeDisposable()

    private val tripDetailsLiveData: MutableLiveData<Resource<Trip>> = MutableLiveData()

    fun observeTripData(): LiveData<Resource<Trip>> {
        return tripDetailsLiveData
    }


    fun getLastTrips(id: Int) {
        tripDetailsLiveData.postValue(Resource(ResourceState.LOADING))
        val disposable = model.getTripDetailsBy(id)
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())
            .subscribe(
                {
                    tripDetailsLiveData.postValue(Resource(ResourceState.SUCCESS, it))
                },
                {
                    tripDetailsLiveData.postValue(Resource(ResourceState.ERROR))
                }
            )

        mDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        mDisposable.clear()
    }
}