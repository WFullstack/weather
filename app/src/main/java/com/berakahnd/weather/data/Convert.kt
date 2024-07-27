package com.berakahnd.weather.data

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Convert {
    // DATE MANAGER
     fun dayOfWeek(date: String): DayOfWeek? {
        val dateString = date
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(dateString, formatter)

        return date.dayOfWeek
    }
    // HOUR MANAGER
     fun hourODay(data : String) : String{
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
        val dateTime = LocalDateTime.parse(data, formatter)
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val timeString = dateTime.format(timeFormatter)
        return timeString.toString()
    }
}