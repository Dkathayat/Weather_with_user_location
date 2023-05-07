package com.kathayat.testWeather.core.data.source.remote.response.currentforecast


data class CurrentWeatherSysResponse (

    val type: Int?,
    val idSys: Int?,
    val country: String?,
    val sunrise: Int?,
    val sunset: Int?
)
