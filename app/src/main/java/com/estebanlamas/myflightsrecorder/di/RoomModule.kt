package com.estebanlamas.myflightsrecorder.di

import androidx.room.Room
import com.estebanlamas.myflightsrecorder.BuildConfig
import com.estebanlamas.myflightsrecorder.data.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import com.estebanlamas.myflightsrecorder.data.db.MIGRATION_1_2

val roomModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, BuildConfig.APPLICATION_ID)
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    single {
        (get() as AppDatabase).flightDAO()
    }

    single {
        (get() as AppDatabase).planePositionDAO()
    }
}