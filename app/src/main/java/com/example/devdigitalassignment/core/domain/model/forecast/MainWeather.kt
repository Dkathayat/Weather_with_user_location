package com.kathayat.testWeather.core.domain.model.forecast

data class MainWeather (

    val temp : Double?,
    val feelsLike : Double?,
    val tempMin : Double?,
    val tempMax: Double?,
    val pressure: Int?,
    val seaLevel: Int?,
    val groundLevel: Int?,
    val humidity: Int?,
    val tempKf: Double?

)
