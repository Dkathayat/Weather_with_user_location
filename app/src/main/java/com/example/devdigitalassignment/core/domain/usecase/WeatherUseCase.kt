package com.kathayat.testWeather.core.domain.usecase

import com.kathayat.testWeather.core.data.Resource
import com.kathayat.testWeather.core.domain.model.currentforecast.CurrentWeather
import com.kathayat.testWeather.core.domain.model.forecast.Forecast
import kotlinx.coroutines.flow.Flow

interface WeatherUseCase {

    fun getForecast(lat: Double, lon: Double): Flow<Resource<Forecast>>

    fun getCurrentForecast(lat: Double, lon: Double): Flow<Resource<CurrentWeather>>
}