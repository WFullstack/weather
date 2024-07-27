package com.berakahnd.weather.data.viewmodel

import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berakahnd.weather.data.RetrofitInstance
import com.berakahnd.weather.data.model.WeatherModel
import com.berakahnd.weather.data.util.NetworkResponse
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale

class WeatherViewModel() : ViewModel() {
    lateinit var context: Context
    private val weatherApi = RetrofitInstance.weatherApi

    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult : LiveData<NetworkResponse<WeatherModel>> = _weatherResult

    var city =  mutableStateOf("Abidjan")

    suspend fun getWeather(latitude: Double, longitude: Double){
        _weatherResult.value = NetworkResponse.Loading
        try {
            viewModelScope.launch {
                try{
                    val response = weatherApi.getWeather(latitude = latitude, longitude = longitude)
                    if(response.isSuccessful){
                        response.body()?.let {
                            _weatherResult.value = NetworkResponse.Success(it)
                        }
                    }else{
                        _weatherResult.value = NetworkResponse.Error("Failed to load data")
                    }
                }
                catch (e : Exception){
                    _weatherResult.value = NetworkResponse.Error("Failed to load data")
                }
        }
        }catch (e : Exception){
            Log.e("ERROR",e.message.toString())
            _weatherResult.value = NetworkResponse.Error("Failed to load data")
        }
    }
    fun GetGPS(geocoder: Geocoder) : Pair<Double,Double> {
        var latitude = 0.0
        var longitude = 0.0
        try {
            val addresses = geocoder.getFromLocationName(city.value, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val location = addresses[0]
                latitude = location.latitude
                longitude = location.longitude
                Log.i(
                    "GPS",
                    "Latitude: ${latitude}, Longitude: ${longitude}}"
                )
                return Pair(first = latitude, second = longitude)
            } else {
                Log.i("GPS", "No location found for the given address.")
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.i("GPS", "${e.message}")
            return Pair(first = latitude, second = longitude)
        }
        return Pair(first = latitude, second = longitude)
    }
}