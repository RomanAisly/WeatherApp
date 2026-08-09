package com.domain.usecases

import com.domain.AppError
import com.domain.CheckDataResult
import com.domain.models.ForecastDetails
import com.domain.repositories.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

class GetForecastUseCase(private val repository: WeatherRepository) {

    suspend operator fun invoke(
        lat: Double,
        lon: Double
    ): Flow<CheckDataResult<ForecastDetails, AppError>> {
        return repository.get10DayForecast(lat, lon).map { result ->
            if (result is CheckDataResult.Success) {
                val forecast = result.data
                val tz = ZoneId.of(forecast.timezone)
                val now = LocalDateTime.now(tz)

                val hourlyList = forecast.hourlyForecast
                    .filter { LocalDateTime.parse(it.time) >= now }
                    .take(24)
                    .mapIndexed { index, item ->
                        val time = LocalDateTime.parse(item.time)

                        item.copy(
                            time = formatHour(time.hour),
                            isNow = index == 0
                        )
                    }

                val dailyList = forecast.dailyForecast.mapIndexed { index, item ->
                    val date = LocalDate.parse(item.date)

                    item.copy(
                        dayOfWeek = date.dayOfWeek.value,
                        isToday = index == 0
                    )
                }

                CheckDataResult.Success(
                    forecast.copy(
                        hourlyForecast = hourlyList,
                        dailyForecast = dailyList
                    )
                )
            } else {
                result
            }
        }.flowOn(Dispatchers.IO)
    }

    private fun formatHour(hour: Int): String {
        val formattedHour = hour.toString().padStart(2, '0')
        return "$formattedHour:00"
    }
}