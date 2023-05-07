package com.kathayat.testWeather.core.utils.networkstatus

sealed class NetworkState {
    object Fetched : NetworkState()
    object Error : NetworkState()
}