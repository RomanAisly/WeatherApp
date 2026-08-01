package com.ui.screens.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel(
//    private val repository: WeatherRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

//    init {
//        // Загружаем погоду при открытии экрана (пока для жестко заданных координат)
//        loadWeather(lat = 51.50, lon = -0.12) // Лондон
//    }

    fun showDialog() {
        _state.update { it.copy(showDialog = true) }
    }

    fun hideDialog() {
        _state.update { it.copy(showDialog = false) }
    }

    fun updateCity(cityName: String) {
        _state.update { it.copy(city = cityName, showDialog = false) }
    }

//    private fun loadWeather(lat: Double, lon: Double) {
//        viewModelScope.launch {
//            repository.getWeather(lat, lon).collect { result ->
//                when (result) {
//                    is CheckDataResult.Success -> {
//                        val weather = result.data
//                        _state.update {
//                            it.copy(
//                                gradus = weather.temperature.roundToInt().toString(),
//                                // Округляем ветер для красивого отображения (например, "12")
//                                wind = weather.windSpeed.roundToInt().toString(),
//                                // Автоматически вычисляем статус для Lottie-анимации!
//                                windStatus = WindStatus.fromSpeed(weather.windSpeed)
//                            )
//                        }
//                    }
//                    is CheckDataResult.Error -> {
//                        // Тут можно как-то обработать ошибку
//                    }
//                }
//            }
//        }
//    }
}