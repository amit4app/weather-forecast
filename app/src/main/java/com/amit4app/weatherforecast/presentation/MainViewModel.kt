package com.amit4app.weatherforecast.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amit4app.weatherforecast.domain.model.Forecast
import com.amit4app.weatherforecast.domain.usecase.GetThreeDayForecastUseCase
import com.amit4app.weatherforecast.domain.usecase.SuggestCitiesUseCase
import com.amit4app.weatherforecast.domain.util.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

data class MainUiState(
    val city: String = "Bengaluru",
    val isLoading: Boolean = false,
    val items: List<UiForecast> = emptyList(),
    val message: String? = null,
    val suggestions: List<String> = emptyList(),
    val showSuggestions: Boolean = false
)

class MainViewModel(
    private val getForecast: GetThreeDayForecastUseCase,
    private val suggestCities: SuggestCitiesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MainUiState())
    val state: StateFlow<MainUiState> = _state

    init {
        val initialCity = "Bengaluru"
        _state.value = _state.value.copy(city = initialCity)
        load()
    }

    private var suggestJob: Job? = null

    fun onQueryChange(q: String) {
        _state.value = _state.value.copy(city = q, message = null)
        // debounce suggestions
        suggestJob?.cancel()
        suggestJob = viewModelScope.launch {
            delay(250)
            val names = runCatching { suggestCities(q) }.getOrDefault(emptyList())
            _state.value = _state.value.copy(
                suggestions = names,
                showSuggestions = names.isNotEmpty()
            )
        }
    }

    fun pickSuggestion(display: String) {
        val onlyCity = display.substringBefore(',').trim()
        _state.value = _state.value.copy(
            city = onlyCity,
            suggestions = emptyList(),
            showSuggestions = false
        )
        load()
    }

    fun load() {
        val city = _state.value.city
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, message = null, showSuggestions = false)
            when (val res = getForecast(city)) {
                is Result.Success -> _state.value = _state.value.copy(
                    isLoading = false,
                    items = res.data.toUi()
                )
                is Result.Error -> {
                    val cachedUi = res.cached?.toUi().orEmpty()
                    _state.value = _state.value.copy(
                        isLoading = false,
                        items = cachedUi,
                        message = if (cachedUi.isNotEmpty())
                            "Offline / last saved data"
                        else
                            (res.throwable.message ?: "No data (check city/network)")
                    )
                }
            }
        }
    }

    private fun Forecast.toUi(): List<UiForecast> =
        days.map {
            UiForecast(
                date = prettyDate(it.dateIso),
                min = "${it.tempMin.roundToInt()}°",
                max = "${it.tempMax.roundToInt()}°",
                ui = mapWeather(it.weatherCode)
            )
        }
}
