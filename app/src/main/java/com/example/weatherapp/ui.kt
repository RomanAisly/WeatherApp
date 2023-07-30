package com.example.weatherapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherapp.data.WeatherModel

@Composable
fun MainList(list: List<WeatherModel>, currentDay: MutableState<WeatherModel>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(list) { _, item ->
            ListItem(item, currentDay)
        }
        
    }
}

@Composable
fun ListItem(item: WeatherModel, currentDay: MutableState<WeatherModel>) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 5.dp)
        .alpha(0.7f)
        .clickable {
            if (item.hours.isEmpty()) return@clickable
            currentDay.value = item
        }, shape = RoundedCornerShape(10.dp), elevation = CardDefaults.cardElevation(20.dp)
    
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.padding(start = 8.dp, top = 5.dp, bottom = 5.dp)) {
                Text(text = item.time,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.Serif,
                    color = Color.Blue)
                Text(text = item.condition,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.Serif,
                    color = Color.Blue)
                
            }
            Text(text = item.currentTemp.ifEmpty { "${item.maxTemp}/${item.minTemp}" },
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.Serif,
                fontSize = 23.sp,
                color = Color.Blue)
            
            AsyncImage(model = "https://cdn.weatherapi.com/weather/64x64/day/116.png",
                contentDescription = "",
                modifier = Modifier
                    .padding(end = 5.dp)
                    .size(35.dp))
        }
        
    }
}

@Composable
fun DialogSearch(dialogState: MutableState<Boolean>, onSubmit: (String) -> Unit) {
    val dialogText = remember {
        mutableStateOf("")
    }
    AlertDialog(onDismissRequest = { dialogState.value = false }, confirmButton = {
        
        TextButton(onClick = {
            onSubmit(dialogText.value)
            dialogState.value = false
        }) {
            Text(text = "OK")
        }
    }, dismissButton = {
        TextButton(onClick = { dialogState.value = false }) {
            Text(text = "Cancel")
        }
    }, title = {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Enter the city name")
            TextField(value = dialogText.value, onValueChange = {
                dialogText.value = it
            })
        }
    })
}
    
