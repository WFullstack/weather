package com.berakahnd.weather.data.model

data class WeatherModel(
    val current: Current = Current(),
    val current_units: CurrentUnits = CurrentUnits(),
    val daily: Daily = Daily(),
    val daily_units: DailyUnits = DailyUnits(),
    val elevation: Double=0.0,
    val generationtime_ms: Double = 0.0,
    val hourly: Hourly = Hourly(),
    val hourly_units: HourlyUnits = HourlyUnits(),
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val timezone: String = "",
    val timezone_abbreviation: String="",
    val utc_offset_seconds: Int=0
)