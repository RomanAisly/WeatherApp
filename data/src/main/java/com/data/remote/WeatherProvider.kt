package com.data.remote

import com.data.remote.dto.ForecastResponseDto
import com.data.remote.dto.GeocodingResponse
import com.data.remote.dto.WeatherResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class WeatherProvider(private val client: HttpClient) {

    suspend fun getCurrentWeather(lat: Double, lon: Double): WeatherResponse {
        return client.get("v1/forecast") {
            url {
                parameters.append("latitude", lat.toString())
                parameters.append("longitude", lon.toString())
                parameters.append(
                    "current",
                    "temperature_2m,wind_speed_10m,weather_code,is_day,precipitation,cloud_cover,uv_index"
                )
                parameters.append("hourly", "wind_speed_10m,weather_code,precipitation")
                parameters.append("forecast_hours", "24")
                parameters.append("timezone", "auto")
            }
        }.body()
    }

    suspend fun get10DayForecast(lat: Double, lon: Double): ForecastResponseDto {
        return client.get("v1/forecast") {
            url {
                parameters.append("latitude", lat.toString())
                parameters.append("longitude", lon.toString())
                parameters.append(
                    "hourly",
                    "temperature_2m,precipitation_probability,weather_code,is_day,wind_speed_10m,precipitation"
                )
                parameters.append("daily", "weather_code,temperature_2m_max,temperature_2m_min")
                parameters.append("forecast_days", "10")
                parameters.append("timezone", "auto")
            }
        }.body()
    }

    suspend fun searchCities(query: String, language: String): GeocodingResponse {
        return client.get("https://geocoding-api.open-meteo.com/v1/search") {
            url {
                parameters.append("name", query)
                parameters.append("count", "5")
                parameters.append("language", language)
            }
        }.body()
    }
}