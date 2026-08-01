package com.data.locale

import com.data.remote.WeatherProvider
import com.data.remote.createHttpClient
import com.data.repositories.WeatherRepositoryImpl
import com.domain.SettingsRepository
import com.domain.WeatherRepository
import io.ktor.client.HttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val settingsModule = module {
    single { androidContext().dataStore }
    single<SettingsRepository> { SettingsManager(get()) }
}

val networkModule = module {
    single<HttpClient> {
        createHttpClient()
    }
    single {
        WeatherProvider(get())
    }
}

val repositoryModule = module {
    single<WeatherRepository> {
        WeatherRepositoryImpl(get())
    }
}