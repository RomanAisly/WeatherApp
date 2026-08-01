package com.weatherapp

import android.app.Application
import com.data.locale.networkModule
import com.data.locale.repositoryModule
import com.data.locale.settingsModule
import com.ui.components.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                settingsModule,
                networkModule,
                repositoryModule,
                viewModelsModule
            )
        }
    }
}