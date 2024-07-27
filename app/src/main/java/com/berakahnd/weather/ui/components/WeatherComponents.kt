package com.berakahnd.weather.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NightlightRound
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.berakahnd.weather.R
import com.berakahnd.weather.data.Convert
import com.berakahnd.weather.data.Datas
import com.berakahnd.weather.data.model.WeatherModel
import com.berakahnd.weather.data.util.NetworkResponse
import com.berakahnd.weather.data.viewmodel.WeatherViewModel

// BODY
@Composable
fun WeatherBody(viewModel: WeatherViewModel,
    result: NetworkResponse.Success<WeatherModel>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "${viewModel.city.value}")
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            val  code = WeatherCode(result.data.current.weather_code)
            WeatherLottie(modifier = Modifier.size(180.dp), code = code)
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "${result.data.current.temperature_2m}°C",fontSize = 38.sp,
                textAlign = TextAlign.Center
            )
        }
        //Divider()
        Text("Current")
        ElevatedCard {
            Column(
                modifier = Modifier.padding(8.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    WeatherParam(
                        imageVector = if(result.data.current.is_day == 0) Icons.Default.NightlightRound else
                            Icons.Default.WbSunny,
                        title = if(result.data.current.is_day == 0) "Night" else "Day",
                        param = "${result.data.current.is_day}"
                    )
                    WeatherParam(imageVector = Icons.Default.Air,
                        title = "Wind", param = "${result.data.current.wind_speed_10m} mph")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    WeatherParam(imageVector = Icons.Default.AccessTime,
                        title = "Time",
                        param = "${result.data.current.time.split("T")[1]}")
                    WeatherParam(imageVector = Icons.Default.WaterDrop,
                        title = "Humidity",
                        param = "${result.data.current.relative_humidity_2m} %")
                }
            }
        }

        //Divider()

        Text("Hour")
        val dataHour = Datas(result = result).hourly()
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            //contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(dataHour){weatherHour ->
                WeatherHour(code = weatherHour.code,
                    period = "${weatherHour.date}", temperature = "${weatherHour.max}°C")
            }
        }
        Text("Daily")
        val dataDaily = Datas(result = result).daily()
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            //contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(dataDaily){weatherDay ->
                WeatherDaily(code = weatherDay.code,
                    period = "${weatherDay.date}",
                    temperatureMax = "${weatherDay.max}°C", temperatureMin = "${weatherDay.min}°C")
            }
        }
    }
}

//WEATHER PARAMATER
@Composable
fun WeatherParam(
    modifier : Modifier = Modifier,
    imageVector: ImageVector,
    title : String, param : String
){
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp /2

    Row(
        modifier = modifier.width(screenWidth),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        Icon(imageVector = imageVector, contentDescription = null)
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = title)
            Text(text = param)
        }
    }
}

// WEATHER DAILY
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherDaily(
    modifier : Modifier = Modifier,
    code: Int,
    period : String,
    temperatureMin : String,
    temperatureMax : String,
){
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp / 5
    val dayOfWeek = Convert.dayOfWeek(period)
    val codeFinal = WeatherCode(code)
    ElevatedCard(onClick = { /*TODO*/ }) {
        Column(
            modifier = modifier.padding(vertical = 8.dp)
                .fillMaxWidth()
                .width(screenWidth),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = dayOfWeek.toString().split("D")[0])
            WeatherLottie(modifier = Modifier.size(40.dp), code = codeFinal)
            Text(text = temperatureMin , color = MaterialTheme.colorScheme.onSurface)
            Text(text = temperatureMax, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }

}

// WEATHER HOUR
@Composable
fun WeatherHour(
    modifier : Modifier = Modifier,
    code: Int,
    period : String,
    temperature : String,
){
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp / 5
    val hour = Convert.hourODay(period)
    val codeFinal = WeatherCode(code)
    OutlinedCard {
        Column(
            modifier = modifier.padding(vertical = 8.dp)
                .fillMaxWidth()
                .width(screenWidth),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = hour, color = MaterialTheme.colorScheme.onSurface)
            WeatherLottie(modifier = Modifier.size(40.dp), code = codeFinal)
            Text(text = temperature, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }

}


//LOTTIE
@Composable
fun WeatherLottie(modifier : Modifier = Modifier, code : Int){
    Box(modifier = modifier){
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(code))
        val progress by animateLottieCompositionAsState(composition,iterations = LottieConstants.IterateForever)
        LottieAnimation(
            composition = composition,
            progress = { progress },
        )
    }
}

// GESTION DU CODE
@Composable
fun WeatherCode(code : Int) : Int{
    var image : Int = 0
    if (code == 0 || code == 1)
    {
        image = R.raw.sun // Clair
    }
    else if (code == 2 || code == 3 || code == 45 || code == 48)
    {
        image = R.raw.cloud // Nuageux
    }
    else if (code == 51 || code == 53 || code == 55 || code == 56 || code == 57)
    {
        image = R.raw.cloud // Bruine
    }
    else if (code == 61 || code == 63 || code == 65 || code == 66 || code == 67)
    {
        image = R.raw.rain // Pluie
    }
    else if (code == 71 || code == 73 || code == 75 || code == 77)
    {
        image = R.raw.snow // Neige
    }
    else if (code == 80 || code == 81 || code == 82 || code == 85 || code == 86)
    {
        image = R.raw.averse // Averses
    }
    else if (code == 95 || code == 96 || code == 99)
    {
        image = R.raw.storm // Orage
    }

    return image
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    //DailyModel(imageVector = Icons.Default.WindPower,title = "Rain", param = "25°C")
}
