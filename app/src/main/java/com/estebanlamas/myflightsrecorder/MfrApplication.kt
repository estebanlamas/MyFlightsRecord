package com.estebanlamas.myflightsrecorder

import android.app.Application
import com.estebanlamas.myflightsrecorder.di.appModule
import com.estebanlamas.myflightsrecorder.di.roomModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MfrApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MfrApplication)
            androidLogger()
            modules(appModule, roomModule)
        }
    }
}