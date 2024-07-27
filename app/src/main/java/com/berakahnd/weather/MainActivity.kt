package com.berakahnd.weather

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.berakahnd.weather.data.util.NetworkResponse
import com.berakahnd.weather.data.viewmodel.WeatherViewModel
import com.berakahnd.weather.ui.components.WeatherBody
import com.berakahnd.weather.ui.theme.WeatherTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewmodel = ViewModelProvider(this)[WeatherViewModel::class.java]
        val geocoder = Geocoder(this)

        weatherManager(viewmodel, geocoder)
        setContent {
            WeatherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(viewModel = viewmodel){
                        weatherManager(viewmodel, geocoder)
                    }
                }
            }
        }
    }

    private fun weatherManager(
        viewmodel: WeatherViewModel,
        geocoder: Geocoder
    ) {
        lifecycleScope.launch {
            try {
                val (latitude, longitude) = viewmodel.GetGPS(geocoder = geocoder)
                viewmodel.getWeather(latitude = latitude, longitude = longitude)
            } catch (e: Exception) {
                Log.e("GPS", "${e.message}")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel : WeatherViewModel,
    modifier: Modifier = Modifier,
    search : () -> Unit
) {
    var search  by rememberSaveable {
        mutableStateOf("")
    }
    val scrollState = rememberScrollState()

    Column(modifier = modifier
        .verticalScroll(scrollState)
        .padding(16.dp)) {
        TextField(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 0.dp, bottom = 16.dp),
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            value = search,
            placeholder = {
                Text(text = "${viewModel.city.value}")
            },
            onValueChange = {
                search = it
            },trailingIcon = {
                Icon(
                    modifier = modifier.clickable {
                        viewModel.city.value = search
                        search()
                    },
                    imageVector = Icons.Default.Search,
                    contentDescription = null)
            },
            colors = TextFieldDefaults.textFieldColors(
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
        val weatherResult = viewModel.weatherResult.observeAsState()
        when(val result = weatherResult.value){
            is NetworkResponse.Error -> {
                Text(text = result.message)
            }
            is NetworkResponse.Loading -> {
                //CircularProgressIndicator()
            }
            is NetworkResponse.Success -> {
                WeatherBody(viewModel = viewModel , result = result)
            }
            null -> {
                //CircularProgressIndicator()
                LinearProgressIndicator(modifier = modifier.fillMaxWidth())
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherTheme {
        //Greeting()
    }
}