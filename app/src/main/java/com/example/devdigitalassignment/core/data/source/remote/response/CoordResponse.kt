package com.kathayat.testWeather.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class CoordResponse (

    @SerializedName("lat")
    val lat: Double?,

    @SerializedName("lon")
    val lon: Double?,
)
