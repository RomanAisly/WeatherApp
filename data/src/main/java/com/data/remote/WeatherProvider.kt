package com.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class WeatherProvider(private val client: HttpClient) {

    suspend fun getCurrentWeather(request: WeatherRequest): WeatherResponse {
        return client.get("v1/forecast") {
            url {
                parameters.append("latitude", request.latitude.toString())
                parameters.append("longitude", request.longitude.toString())
                parameters.append("current", request.current)
            }
        }.body()
    }
}