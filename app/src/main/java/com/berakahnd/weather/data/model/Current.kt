package com.berakahnd.weather.data.model

data class Current(
    val interval: Int = 0,
    val is_day: Int = 0,
    val relative_humidity_2m: Int = 0,
    val temperature_2m: Double = 0.0,
    val time: String = "",
    val weather_code: Int = 0,
    val wind_speed_10m: Double = 0.0
)