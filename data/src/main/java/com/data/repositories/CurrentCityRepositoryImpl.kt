package com.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.domain.models.CityItem
import com.domain.repositories.CurrentCityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CurrentCityRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : CurrentCityRepository {

    companion object {
        private val CITY_ID = intPreferencesKey("city_id")
        private val CITY_NAME = stringPreferencesKey("city_name")
        private val CITY_COUNTRY = stringPreferencesKey("city_country")
        private val CITY_FLAG = stringPreferencesKey("city_flag")
        private val CITY_LAT = doublePreferencesKey("city_lat")
        private val CITY_LON = doublePreferencesKey("city_lon")
    }

    override val selectedCity: Flow<CityItem?> = dataStore.data.map { prefs ->
        val id = prefs[CITY_ID]
        val name = prefs[CITY_NAME]
        val country = prefs[CITY_COUNTRY]
        val flag = prefs[CITY_FLAG]
        val lat = prefs[CITY_LAT]
        val lon = prefs[CITY_LON]

        if (id != null && name != null && country != null && flag != null && lat != null && lon != null) {
            CityItem(id, name, country, flag, lat, lon)
        } else {
            null
        }
    }

    override suspend fun setCity(city: CityItem) {
        dataStore.edit { prefs ->
            prefs[CITY_ID] = city.id
            prefs[CITY_NAME] = city.name
            prefs[CITY_COUNTRY] = city.country
            prefs[CITY_FLAG] = city.flagEmoji
            prefs[CITY_LAT] = city.latitude
            prefs[CITY_LON] = city.longitude
        }
    }
}