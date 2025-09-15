package com.amit4app.weatherforecast.domain.usecase

import com.amit4app.weatherforecast.data.local.CityDao
import com.amit4app.weatherforecast.data.local.CityEntity
import com.amit4app.weatherforecast.data.remote.GeoApi

class SuggestCitiesUseCase(
    private val cityDao: CityDao,
    private val geo: GeoApi
) {
    suspend operator fun invoke(query: String): List<String> {
        val q = query.trim()
        // If blank, show recents (offline)
        if (q.isBlank()) {
            return cityDao.recent().map { it.label() }
        }

        // Always start with offline suggestions
        val offline = cityDao.suggestByPrefix(q).map { it.label() }

        // Try online (if offline, network call will throw and we keep offline only)
        val online = try {
            geo.search(q, count = 10).results.orEmpty()
                .map { r ->
                    listOfNotNull(r.name, r.admin1, r.country).joinToString(", ")
                }
        } catch (_: Throwable) {
            emptyList()
        }

        // Merge while keeping order: offline first, then unique online
        val seen = LinkedHashSet<String>()
        val merged = ArrayList<String>()
        (offline + online).forEach {
            if (seen.add(it)) merged.add(it)
        }
        return merged.take(20)
    }

    private fun CityEntity.label(): String =
        listOfNotNull(name, admin1, country).joinToString(", ")
}
