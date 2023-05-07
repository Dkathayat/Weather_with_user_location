package com.example.devdigitalassignment.core.ui.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class LiveLocationViewModel(application: Application): AndroidViewModel(application) {
    private val locationLiveData = LocationLiveData(application)
    internal fun getLocationLiveData() = locationLiveData
}