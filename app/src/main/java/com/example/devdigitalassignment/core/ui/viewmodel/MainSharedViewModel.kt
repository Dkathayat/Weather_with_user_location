package com.example.devdigitalassignment.core.ui.viewmodel

import androidx.lifecycle.*
import com.example.devdigitalassignment.core.data.WeatherRepository
import com.google.android.gms.maps.model.LatLng
import com.kathayat.testWeather.core.data.source.local.LocalDataSource
import com.kathayat.testWeather.core.data.source.local.entity.currentforecast.CurrentWeatherEntity
import com.kathayat.testWeather.core.data.source.remote.RemoteDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.Flow
import javax.inject.Inject

@HiltViewModel
class MainSharedViewModel @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    private val _location = MutableLiveData<LatLng>()
    val location: LiveData<LatLng> = _location

    private var _allUserData = MutableLiveData<List<CurrentWeatherEntity>>()
    var allUserData: LiveData<List<CurrentWeatherEntity>> = _allUserData

    private val _address = MutableLiveData<String>()
    val address: LiveData<String> = _address

    var city = ""
    var geoPoint: LatLng? = null

    init {
        getAllUserData()
    }


    fun setLocationAddress(name: String) {
        _address.value = name
    }

    fun setLatLang(latLng: LatLng) {
        _location.value = latLng
    }

    private fun getAllUserData() = viewModelScope.launch {
        allUserData = weatherRepository.getAllUserData()

    }
}