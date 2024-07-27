package com.berakahnd.weather.data.model

data class Hourly(
    val temperature_2m: List<Double> = emptyList(),
    val time: List<String> = emptyList(),
    val weather_code: List<Int> = emptyList()
)