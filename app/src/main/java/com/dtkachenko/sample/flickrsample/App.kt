package com.dtkachenko.sample.flickrsample

import android.app.Application
import com.dtkachenko.sample.flickrsample.api.networkModule
import com.dtkachenko.sample.flickrsample.system.systemModule
import org.koin.android.ext.android.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(listOf(systemModule, appModule, networkModule))
    }

}