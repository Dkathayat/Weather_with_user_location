package com.kathayat.testWeather.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kathayat.testWeather.core.utils.networkstatus.NetworkState
import com.kathayat.testWeather.core.utils.networkstatus.NetworkStatusTracker
import com.kathayat.testWeather.core.utils.networkstatus.map
import kotlinx.coroutines.Dispatchers

class NetworkStatusViewModel(
    networkStatusTracker: NetworkStatusTracker,
) : ViewModel() {

    val state =
        networkStatusTracker.networkStatus
            .map(
                onUnavailable = { NetworkState.Error },
                onAvailable = { NetworkState.Fetched },
            )
            .asLiveData(Dispatchers.IO)
}