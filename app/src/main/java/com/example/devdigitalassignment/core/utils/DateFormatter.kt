package com.kathayat.testWeather.core.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import java.util.*


object DateFormatter {

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDay(date: String): String? {

        val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val localDateTime = LocalDateTime.parse(date, pattern)

        return localDateTime.format(DateTimeFormatter.ofPattern("EEEE"))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getHour(date: String): String? {

        val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val localDateTime = LocalDateTime.parse(date, pattern)

        return localDateTime.format(DateTimeFormatter.ofPattern("HH.mm"))
    }

    fun getDayNHour(timestamp: Long): String? {

        val format = "EEEE, HH.mm"
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()

        return sdf.format(Date(timestamp * 1000))
    }
}