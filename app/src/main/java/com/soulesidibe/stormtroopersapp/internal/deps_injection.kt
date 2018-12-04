package com.soulesidibe.stormtroopersapp.internal

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.soulesidibe.stormtroopersapp.BaseSchedulerProvider
import com.soulesidibe.stormtroopersapp.BuildConfig
import com.soulesidibe.stormtroopersapp.SchedulerProvider
import com.soulesidibe.stormtroopersapp.model.*
import com.soulesidibe.stormtroopersapp.view.activity.LastTripsActivity
import com.soulesidibe.stormtroopersapp.view.activity.TripDetailsActivity
import com.soulesidibe.stormtroopersapp.viewmodel.LastTripsViewModel
import com.soulesidibe.stormtroopersapp.viewmodel.TripDetailsViewModel
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

/**
 * Created on 11/24/18 at 10:51 PM
 * Project name : StormTroopersApp
 */


@Module
class NetWorkModule {

    @Provides
    fun provideOkHttp(): OkHttpClient {
        var okHttpClient = OkHttpClient()
        if (BuildConfig.DEBUG) {
            val httpLogging = HttpLoggingInterceptor().let {
                it.level = HttpLoggingInterceptor.Level.BODY
                it
            }

            okHttpClient = okHttpClient.newBuilder()
                .addNetworkInterceptor(httpLogging)
                .build()
        }
        return okHttpClient
    }

    @Provides
    fun provideRetrofit(okHttp: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_SERVER)
            .client(okHttp)
            .addConverterFactory(MoshiConverterFactory.create())
            .build() as Retrofit
    }

    @Provides
    fun provideAPI(retrofit: Retrofit) = retrofit.create(StarWarsAPI::class.java)
}

@Module(includes = [NetWorkModule::class, CommonModule::class, ContextModule::class])
class LastTripModule {

    @Provides
    fun provideLastTripModel(
        api: StarWarsAPI,
        internetManager: InternetManager
    ): LastTripsModel {
        return LastTripsModelImpl(api, internetManager)
    }

    @Provides
    fun provideLastTripViewModelFactory(
        model: LastTripsModel,
        scheduler: BaseSchedulerProvider
    ): LastTripViewModelFactory {
        return LastTripViewModelFactory(model, scheduler)
    }

    @Provides
    fun provideLastTripViewModel(lastTripViewModelFactory: LastTripViewModelFactory) =
        lastTripViewModelFactory.create(LastTripsViewModel::class.java)
}

@Module(includes = [NetWorkModule::class, CommonModule::class, ContextModule::class])
class TripDetailModule {
    @Provides
    fun provideTripDetailModel(
        api: StarWarsAPI,
        internetManager: InternetManager
    ): TripDetailsModel = TripDetailsModelImpl(api, internetManager)

    @Provides
    fun provideTripDetailsViewModelFactory(
        model: TripDetailsModel,
        scheduler: BaseSchedulerProvider
    ): TripDetailsViewModelFactory {
        return TripDetailsViewModelFactory(model, scheduler)
    }

    @Provides
    fun provideTripDetailsViewModel(TripDetailsViewModelFactory: TripDetailsViewModelFactory) =
        TripDetailsViewModelFactory.create(TripDetailsViewModel::class.java)
}

@Module
class CommonModule {

    @Provides
    fun provideScheduler(): BaseSchedulerProvider = SchedulerProvider()
}

@Module
class ContextModule(val context: Context) {

    @Provides
    fun provideInternetManager(): InternetManager = AndroidInternetManager(context)
}

class LastTripViewModelFactory @Inject constructor(
    private val model: LastTripsModel,
    private val schedulerProvider: BaseSchedulerProvider
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LastTripsViewModel::class.java)) {
            return LastTripsViewModel(model, schedulerProvider) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}

class TripDetailsViewModelFactory @Inject constructor(
    private val model: TripDetailsModel,
    private val schedulerProvider: BaseSchedulerProvider
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TripDetailsViewModel::class.java)) {
            return TripDetailsViewModel(model, schedulerProvider) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}

@Component(modules = [LastTripModule::class])
interface LastTripComponent {
    fun inject(activity: LastTripsActivity)
}

@Component(modules = [TripDetailModule::class])
interface TripDetailsComponent {
    fun inject(activity: TripDetailsActivity)
}