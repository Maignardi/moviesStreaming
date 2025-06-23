package com.maignardi.moviestreamingapp

import android.app.Application
import com.maignardi.moviestreamingapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MovieApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MovieApp)
            modules(appModule)
        }
    }
}