package com.kathayat.testWeather.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class SysResponse (

    @SerializedName("pod")
    val pod: String?
)
