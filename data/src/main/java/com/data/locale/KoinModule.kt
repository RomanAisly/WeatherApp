package com.data.locale

import com.data.remote.WeatherProvider
import com.data.remote.createHttpClient
import com.data.repositories.CurrentCityRepositoryImpl
import com.data.repositories.WeatherRepositoryImpl
import com.domain.repositories.CurrentCityRepository
import com.domain.repositories.SettingsRepository
import com.domain.repositories.WeatherRepository
import com.domain.usecases.GetForecastUseCase
import com.domain.usecases.GetLiveTimeUseCase
import com.domain.usecases.GetWeatherDetailsUseCase
import io.ktor.client.HttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataStoreModule = module {
    single { androidContext().dataStore }
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
    single<WeatherRepository> { WeatherRepositoryImpl(get(), get()) }
    single<SettingsRepository> { SettingsManager(get()) }
    single<CurrentCityRepository> { CurrentCityRepositoryImpl(get()) }
}

val useCaseModule = module {
    factory { GetWeatherDetailsUseCase(get()) }
    factory { GetLiveTimeUseCase() }
    factory { GetForecastUseCase(get()) }
}