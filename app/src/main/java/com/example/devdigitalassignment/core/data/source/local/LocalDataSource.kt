package com.kathayat.testWeather.core.data.source.local

import androidx.lifecycle.LiveData
import com.example.devdigitalassignment.core.data.source.local.entity.UserWeatherTable
import com.kathayat.testWeather.core.data.source.local.entity.currentforecast.CurrentWeatherEntity
import com.example.devdigitalassignment.core.data.source.local.entity.forecast.ForecastEntity
import com.example.devdigitalassignment.core.data.source.local.room.ForecastDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(private val forecastDao: ForecastDao) {

    companion object {
        private var instance: LocalDataSource? = null

        fun getInstance(forecastDao: ForecastDao): LocalDataSource =
            instance ?: synchronized(this) {
                instance ?: LocalDataSource(forecastDao)
            }
    }

    fun getForecast(): Flow<ForecastEntity> = forecastDao.getForecast()

    suspend fun insertForecast(forecast: ForecastEntity) = forecastDao.insertForecast(forecast)

    fun getCurrentForecast(): Flow<CurrentWeatherEntity> = forecastDao.getCurrentForecast()

    suspend fun insertCurrentForecast(forecast: CurrentWeatherEntity) = forecastDao.insertCurrentForecast(forecast)


    fun getAllUserData(): LiveData<List<CurrentWeatherEntity>> = forecastDao.getAllByLiveData()
}