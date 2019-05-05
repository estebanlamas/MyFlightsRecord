package com.estebanlamas.myflightsrecorder.di

import com.estebanlamas.myflightsrecorder.data.FlightsDataRepository
import com.estebanlamas.myflightsrecorder.data.GoogleLocationRepository
import com.estebanlamas.myflightsrecorder.data.db.FlightDAO
import com.estebanlamas.myflightsrecorder.data.db.PlanePositionDAO
import com.estebanlamas.myflightsrecorder.domain.repository.FlightRepository
import com.estebanlamas.myflightsrecorder.domain.repository.LocationRepository
import com.estebanlamas.myflightsrecorder.presentation.flights.FlightsListPresenter
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single<FlightRepository> { FlightsDataRepository(get() as FlightDAO, get() as PlanePositionDAO) }

    single<LocationRepository> { GoogleLocationRepository(androidContext()) }

    single { FlightsListPresenter(get() as FlightRepository) }
}