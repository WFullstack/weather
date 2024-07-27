package com.berakahnd.weather.data.model

data class CurrentUnits(
    val interval: String ="",
    val is_day: String="",
    val relative_humidity_2m: String="",
    val temperature_2m: String="",
    val time: String="",
    val weather_code: String="",
    val wind_speed_10m: String=""
)