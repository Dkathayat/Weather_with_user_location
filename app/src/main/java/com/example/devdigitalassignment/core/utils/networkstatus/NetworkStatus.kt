package com.kathayat.testWeather.core.utils.networkstatus

sealed class NetworkStatus {
    object Available : NetworkStatus()
    object Unavailable : NetworkStatus()
}