package com.berakahnd.weather.data.model

data class Daily(
    val temperature_2m_max: List<Double> = emptyList(),
    val temperature_2m_min: List<Double> = emptyList(),
    val time: List<String> = emptyList(),
    val weather_code: List<Int> = emptyList()
)