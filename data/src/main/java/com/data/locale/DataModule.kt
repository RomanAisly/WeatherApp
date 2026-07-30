package com.data.locale

import com.data.remote.createHttpClient
import com.domain.SettingsRepository
import io.ktor.client.HttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val settingsModule = module {
    single { androidContext().dataStore }
    single<SettingsRepository> { SettingsManager(get()) }
}

val networkModule = module {
    single<HttpClient> {
        createHttpClient()
    }
}

////days in param
//private fun getData(city: String,
//                    context: Context,
//                    daysList: MutableState<List<WeatherModel>>,
//                    currentDay: MutableState<WeatherModel>) {
//    val url =
//        "https://api.weatherapi.com/v1/forecast.json?key=$API_KEY&q=$city&days=3&aqi=no&alerts=no"
//
//    val queue = Volley.newRequestQueue(context)
//    val strRequest = StringRequest(Request.Method.GET, url, { response ->
//        val list = getWeatherByDays(response)
//        currentDay.value = list[0]
//        daysList.value = list
//    }, { Log.d("MyLog", "errorVolley: $it") })
//    queue.add(strRequest)
//}
//
//private fun getWeatherByDays(response: String): List<WeatherModel> {
//    if (response.isEmpty()) return listOf()
//    val list = ArrayList<WeatherModel>()
//    val mainObject = JSONObject(response)
//    val city = mainObject.getJSONObject("location").getString("name")
//    val days = mainObject.getJSONObject("forecast").getJSONArray("forecastday")
//    for (i in 0 until days.length()) {
//        val item = days[i] as JSONObject
//        list.add(WeatherModel(city,
//            item.getString("date"),
//            "",
//            item.getJSONObject("day").getJSONObject("condition").getString("text"),
//            item.getJSONObject("day").getJSONObject("condition").getString("icon"),
//            item.getJSONObject("day").getString("maxtemp_c"),
//            item.getJSONObject("day").getString("mintemp_c"),
//            item.getJSONArray("hour").toString()))
//
//    }
//    list[0] = list[0].copy(
//        time = mainObject.getJSONObject("current").getString("last_updated"),
//        currentTemp = mainObject.getJSONObject("current").getString("temp_c"),
//    )
//    return list
//}