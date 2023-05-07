package com.kathayat.testWeather.core.domain.model.forecast

import com.kathayat.testWeather.core.domain.model.City

data class Forecast (

    val city: City?,
    val cnt: Int?,
    val cod: String?,
    val message: Double?,
    val list: List<ListForecast>?
)