package com.berakahnd.weather.data

import com.berakahnd.weather.data.model.DailyModel
import com.berakahnd.weather.data.model.HourlyModel
import com.berakahnd.weather.data.model.WeatherModel
import com.berakahnd.weather.data.util.NetworkResponse

class Datas(result: NetworkResponse.Success<WeatherModel>) {
    private val daily_max = result.data.daily.temperature_2m_max
    private val daily_min = result.data.daily.temperature_2m_min
    private val daily_code = result.data.daily.weather_code
    private val daily_time = result.data.daily.time

    private val hour_max = result.data.hourly.temperature_2m
    private val hour_code = result.data.hourly.weather_code
    private val hour_time = result.data.hourly.time

    private var dataDaily : List<DailyModel> = emptyList()
    private var dataHour : List<HourlyModel> = emptyList()

    fun daily() : List<DailyModel>{
        for (index in 0 until 7){
            dataDaily += DailyModel(min = daily_min[index],max = daily_max[index]
                ,code = daily_code[index], date = daily_time[index])
        }
        return  dataDaily
    }

    fun hourly() : List<HourlyModel>{
        for (index in 0 until 24){
            dataHour += HourlyModel(max = hour_max[index]
                ,code = hour_code[index], date = hour_time[index])
        }
        return  dataHour
    }
}