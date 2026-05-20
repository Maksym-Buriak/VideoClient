package com.maks_buriak.videoclient.app

import android.app.Application
import com.maks_buriak.videoclient.di.appModule
import com.maks_buriak.videoclient.di.dataModule
import com.maks_buriak.videoclient.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(listOf(appModule, domainModule, dataModule))
        }
        /*
            1. UI (View) створює ViewModel через DI
            2. ViewModel отримує UseCase через DI
            3. UseCase отримує Repository через DI
            4. Repository отримує Storage через DI
        */
    }
}