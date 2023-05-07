package com.kathayat.testWeather.core.data.source.remote.response.forecast

import com.google.gson.annotations.SerializedName
import com.kathayat.testWeather.core.data.source.remote.response.CityResponse

data class ForecastResponse (

    @SerializedName("city")
    val city: CityResponse?,

    @SerializedName("cnt")
    val cnt: Int?,

    @SerializedName("cod")
    val cod: String?,

    @SerializedName("message")
    var message: Double?,

    @SerializedName("list")
    val list: List<ListForecastResponse>?
)