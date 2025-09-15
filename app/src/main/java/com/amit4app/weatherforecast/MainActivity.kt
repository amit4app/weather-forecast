@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.amit4app.weatherforecast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.view.WindowCompat
import com.amit4app.weatherforecast.data.DefaultWeatherRepository
import com.amit4app.weatherforecast.data.remote.ApiFactory
import com.amit4app.weatherforecast.domain.usecase.GetThreeDayForecastUseCase
import com.amit4app.weatherforecast.domain.usecase.SuggestCitiesUseCase
import com.amit4app.weatherforecast.presentation.MainViewModel
import com.amit4app.weatherforecast.presentation.UiForecast
import com.amit4app.weatherforecast.presentation.theme.WeatherTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // keep first
        // Edge-to-edge (and prevent overlap using WindowInsets.safeDrawing in Scaffold)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()

        val app = application as WeatherApp

        // Simple DI
        val repo = DefaultWeatherRepository(
            forecastDao = app.db.forecastDao(),
            cityDao = app.db.cityDao(),
            geoApi = ApiFactory.geo,
            weatherApi = ApiFactory.weather
        )
        val getForecast = GetThreeDayForecastUseCase(repo)
        val suggestCities = SuggestCitiesUseCase(app.db.cityDao(), ApiFactory.geo)
        val vm = MainViewModel(getForecast, suggestCities)

        setContent {
            WeatherTheme {
                Scaffold(
                    contentWindowInsets = WindowInsets.safeDrawing, // ← fixes status bar overlap
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text("3-Day Forecast") }
                        )
                    }
                ) { inner ->
                    val state by vm.state.collectAsState()

                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(inner)
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Column(Modifier.fillMaxSize()) {
                            // Search row
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = state.city,
                                    onValueChange = { vm.onQueryChange(it) },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    label = { Text("City") },
                                )
                                Spacer(Modifier.width(8.dp))
                                FilledTonalButton(onClick = { vm.load() }) {
                                    Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                                    Spacer(Modifier.width(6.dp))
                                    Text("Refresh")
                                }
                            }

                            // Suggestions (online or offline based on your VM logic)
                            if (state.showSuggestions && state.suggestions.isNotEmpty()) {
                                SuggestionsList(
                                    items = state.suggestions,
                                    onPick = { vm.pickSuggestion(it) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 6.dp)
                                        .zIndex(1f)
                                )
                            }

                            // Message
                            state.message?.let {
                                Spacer(Modifier.height(8.dp))
                                Text(it, color = MaterialTheme.colorScheme.error)
                            }

                            Spacer(Modifier.height(12.dp))

                            // List / loader
                            if (state.isLoading && state.items.isEmpty()) {
                                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator()
                                }
                            } else {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(bottom = 24.dp)
                                ) {
                                    items(state.items) { f -> ForecastRow(f) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SuggestionsList(
    items: List<String>,
    onPick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier) {
        Column(Modifier.fillMaxWidth()) {
            items.take(8).forEach { name ->
                ListItem(
                    headlineContent = { Text(name) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onPick(name) }
                )
                Divider()
            }
        }
    }
}

@Composable
private fun ForecastRow(f: UiForecast) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(f.ui.icon, contentDescription = f.ui.label)
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(f.date, style = MaterialTheme.typography.titleMedium)
                Text(f.ui.label, style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(Modifier.width(12.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text("Max ${f.max}", style = MaterialTheme.typography.bodyMedium)
                Text("Min ${f.min}", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
