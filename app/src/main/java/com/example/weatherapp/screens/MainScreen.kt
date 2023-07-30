package com.example.weatherapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherapp.MainList
import com.example.weatherapp.R
import com.example.weatherapp.data.WeatherModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

@Composable
fun MainCard(currentDay: MutableState<WeatherModel>,
             onclickSync: () -> Unit,
             onclickSearch: () -> Unit) {
    Column(modifier = Modifier.padding(7.dp)) {
        Card(modifier = Modifier
            .fillMaxWidth()
            .alpha(0.6f), shape = RoundedCornerShape(20.dp),
        contentColor = Color.Blue) {
            Column(modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(modifier = Modifier.padding(top = 8.dp, start = 8.dp),
                        text = currentDay.value.time,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Serif)
                    AsyncImage(model = "https:" + currentDay.value.icon,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(5.dp)
                            .size(35.dp))
                }
                Text(text = currentDay.value.city,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Serif)
                Text(text = if (currentDay.value.currentTemp.isNotEmpty()) {
                    currentDay.value.currentTemp.toFloat().toInt().toString() + "°C"
                } else {
                    currentDay.value.maxTemp.toFloat().toInt()
                        .toString() + "°C/${currentDay.value.minTemp.toFloat().toInt()}°C"
                }, fontSize = 65.sp, fontWeight = FontWeight.Medium, fontFamily = FontFamily.Serif)
                Text(text = currentDay.value.condition,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Serif)
                
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    IconButton(onClick = {
                        onclickSearch.invoke()
                    }) {
                        Icon(painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = stringResource(R.string.cont_desc_search))
                    }
                    Text(text = "${
                        currentDay.value.maxTemp.toFloat().toInt()
                    }°C/${currentDay.value.minTemp.toFloat().toInt()}°C",
                        Modifier.padding(top = 10.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Serif)
                    IconButton(onClick = {
                        onclickSync.invoke()
                    }) {
                        Icon(painter = painterResource(id = R.drawable.ic_sync),
                            contentDescription = stringResource(R.string.cont_desc_synchronize))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabLayout(daysList: MutableState<List<WeatherModel>>, currentDay: MutableState<WeatherModel>) {
    
    val tablist =
        listOf(stringResource(R.string.pager_bar_hours), stringResource(R.string.pager_bar_days))
    val pagerState = rememberPagerState()
    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()
    
    Column(modifier = Modifier
        .padding(start = 7.dp, end = 7.dp)
        .clip(RoundedCornerShape(10.dp))
        .alpha(0.7f)) {
        TabRow(selectedTabIndex = tabIndex, indicator = { pos ->
            TabRowDefaults.Indicator(Modifier.pagerTabIndicatorOffset(pagerState, pos))
        }) {
            tablist.forEachIndexed { index, text ->
                Tab(selected = false, onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }, text = {
                    Text(text = text,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Serif)
                })
            }
        }
        HorizontalPager(count = tablist.size,
            state = pagerState,
            modifier = Modifier.weight(1f)) { index ->
            val list = when (index) {
                0 -> getWeatherByHours(currentDay.value.hours)
                1 -> daysList.value
                else -> daysList.value
            }
            MainList(list, currentDay)
        }
    }
}

private fun getWeatherByHours(hours: String): List<WeatherModel> {
    if (hours.isEmpty()) return listOf()
    val hoursArray = JSONArray(hours)
    val list = ArrayList<WeatherModel>()
    for (i in 0 until hoursArray.length()) {
        val item = hoursArray[i] as JSONObject
        list.add(WeatherModel("",
            item.getString("time"),
            item.getString("temp_c").toFloat().toInt().toString() + "°C",
            item.getJSONObject("condition").getString("text"),
            item.getJSONObject("condition").getString("icon"),
            "",
            "",
            ""))
        
    }
    return list
}