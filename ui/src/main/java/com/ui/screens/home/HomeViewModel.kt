package com.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.AppError
import com.domain.CheckDataResult
import com.domain.LocationResult
import com.domain.LocationTracker
import com.domain.models.CityItem
import com.domain.repositories.CurrentCityRepository
import com.domain.repositories.WeatherRepository
import com.domain.usecases.GetLiveTimeUseCase
import com.domain.usecases.GetWeatherDetailsUseCase
import com.ui.components.PrecipitationType
import com.ui.components.UvStatus
import com.ui.components.WeatherType
import com.ui.components.WindStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

class HomeViewModel(
    private val repository: WeatherRepository,
    private val getWeatherDetailsUseCase: GetWeatherDetailsUseCase,
    private val getLiveTimeUseCase: GetLiveTimeUseCase,
    private val currentCityRepository: CurrentCityRepository,
    private val locationTracker: LocationTracker
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _snack = Channel<AppError>(Channel.BUFFERED)
    val snack = _snack.receiveAsFlow()

    private val searchQueryFlow = MutableStateFlow("")
    private var timeJob: Job? = null

    private var lastUpdateTime: Long = 0
    private val updateIntervalMillis = 10.minutes.inWholeMilliseconds

    private var isWaitingForGpsSettings = false

    init {
        observeSearch()
        checkFirstLaunch()
        observeCurrentCity()
    }

    fun showCitySearchOverlay() {
        _state.update { it.copy(showDialog = true) }
    }

    fun closeCitySearchOverlay() {
        _state.update {
            it.copy(showDialog = false, searchQuery = "", suggestedCities = emptyList())
        }
    }

    fun onResumeApp() {
        if (isWaitingForGpsSettings) {
            isWaitingForGpsSettings = false
            _state.update { it.copy(askForLocationPermission = true) }
        } else {
            refreshWeather()
        }
    }

    fun dismissGpsWarning(goToSettings: Boolean) {
        _state.update { it.copy(showGpsWarning = false) }

        if (goToSettings) {
            isWaitingForGpsSettings = true
        } else {
            showCitySearchOverlay()
        }
    }

    fun onSearchQueryChanged(query: String) {
        _state.update { it.copy(searchQuery = query) }
        searchQueryFlow.value = query
    }

    fun updateCity(city: CityItem) {
        _state.update {
            it.copy(
                showDialog = false,
                searchQuery = "",
                suggestedCities = emptyList()
            )
        }
        viewModelScope.launch {
            currentCityRepository.setCity(city)
        }
    }

    fun refreshWeather(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            val currentCity = currentCityRepository.selectedCity.firstOrNull() ?: return@launch

            val currentTime = System.currentTimeMillis()
            val timeSinceLastUpdate = currentTime - lastUpdateTime

            if (!forceRefresh && timeSinceLastUpdate < updateIntervalMillis) {
                return@launch
            }
            loadWeather(currentCity.latitude, currentCity.longitude)
        }
    }

    fun onPermissionResult(isGranted: Boolean) {
        _state.update { it.copy(askForLocationPermission = false) }

        if (isGranted) {
            _state.update { it.copy(isLocating = true) }

            viewModelScope.launch {
                when (val result = locationTracker.getCurrentLocation()) {
                    is LocationResult.Success -> {
                        _state.update { it.copy(isLocating = false) }
                        updateCity(result.city)
                    }

                    is LocationResult.GpsDisabled -> {
                        _state.update { it.copy(isLocating = false, showGpsWarning = true) }
                    }

                    else -> {
                        _state.update { it.copy(isLocating = false) }
                        showCitySearchOverlay()
                    }
                }
            }
        } else {
            showCitySearchOverlay()
        }
    }

    private fun observeCurrentCity() {
        viewModelScope.launch {
            currentCityRepository.selectedCity
                .filterNotNull()
                .collectLatest { city ->
                    _state.update { it.copy(city = city.name) }
                    loadWeather(city.latitude, city.longitude)
                }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun observeSearch() {
        viewModelScope.launch {
            searchQueryFlow
                .debounce(300.milliseconds)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    if (query.isBlank()) {
                        flowOf(emptyList())
                    } else {
                        repository.searchCities(query).map { result ->
                            when (result) {
                                is CheckDataResult.Success -> result.data
                                is CheckDataResult.Error -> {
                                    _snack.send(result.error)
                                    emptyList()
                                }
                            }
                        }
                    }
                }
                .collect { cities ->
                    _state.update { it.copy(suggestedCities = cities) }
                }
        }
    }

    private fun loadWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            getWeatherDetailsUseCase(lat, lon).collect { result ->
                when (result) {
                    is CheckDataResult.Success -> {
                        lastUpdateTime = System.currentTimeMillis()
                        val details = result.data
                        _state.update {
                            it.copy(
                                gradus = details.weather.temperature.roundToInt().toString(),
                                wind = details.weather.windSpeed.roundToInt().toString(),
                                windStatus = WindStatus.fromSpeed(details.weather.windSpeed),
                                precipType = PrecipitationType.fromWmoCode(details.weather.weatherCode),
                                weatherType = WeatherType.fromWmoCode(
                                    details.weather.weatherCode,
                                    details.weather.isDay
                                ),
                                cloudCover = "${details.weather.cloudCover} %",
                                precipAmount = "${details.weather.precipitation} mm",
                                windDuration = details.windDuration,
                                precipDuration = details.precipDuration,
                                weatherDuration = details.weatherDuration,
                                uvIndex = details.weather.uvIndex.toString(),
                                uvStatus = UvStatus.fromIndex(details.weather.uvIndex)
                            )
                        }
                        startLiveClock(details.weather.timezone)
                    }

                    is CheckDataResult.Error -> {
                        _snack.send(result.error)
                    }
                }
            }
        }
    }

    private fun startLiveClock(timezone: String) {
        timeJob?.cancel()
        timeJob = viewModelScope.launch {
            getLiveTimeUseCase(timezone).collect { newTime ->
                _state.update { it.copy(currentTime = newTime) }
            }
        }
    }

    private fun checkFirstLaunch() {
        viewModelScope.launch {
            val savedCity = currentCityRepository.selectedCity.firstOrNull()
            if (savedCity == null) {
                _state.update { it.copy(askForLocationPermission = true) }
            }
        }
    }
}