package com.kathayat.testWeather.core.domain.repository

import com.kathayat.testWeather.core.data.Resource
import com.kathayat.testWeather.core.domain.model.currentforecast.CurrentWeather
import com.kathayat.testWeather.core.domain.model.forecast.Forecast
import kotlinx.coroutines.flow.Flow

interface IWeatherRepository {

    fun getForecast(lat: Double, lon: Double): Flow<Resource<Forecast>>

    fun getCurrentForecast(lat: Double, lon: Double): Flow<Resource<CurrentWeather>>
}