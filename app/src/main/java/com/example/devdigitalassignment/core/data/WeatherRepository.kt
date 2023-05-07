package com.example.devdigitalassignment.core.data

import com.kathayat.testWeather.core.data.NetworkBoundResource
import com.kathayat.testWeather.core.data.Resource
import com.kathayat.testWeather.core.data.source.local.LocalDataSource
import com.kathayat.testWeather.core.data.source.remote.RemoteDataSource
import com.kathayat.testWeather.core.data.source.remote.network.ApiResponse
import com.kathayat.testWeather.core.data.source.remote.response.currentforecast.CurrentWeatherResponse
import com.kathayat.testWeather.core.data.source.remote.response.forecast.ForecastResponse
import com.kathayat.testWeather.core.domain.model.currentforecast.CurrentWeather
import com.kathayat.testWeather.core.domain.model.forecast.Forecast
import com.kathayat.testWeather.core.domain.repository.IWeatherRepository
import com.kathayat.testWeather.core.utils.AppExecutors
import com.kathayat.testWeather.core.utils.datamapper.MapperEntityToDomain
import com.kathayat.testWeather.core.utils.datamapper.MapperResponseToEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : IWeatherRepository {

    override fun getForecast(lat: Double, lon: Double): Flow<Resource<Forecast>> =
        object : NetworkBoundResource<Forecast, ForecastResponse>() {

            override fun loadFromDB(): Flow<Forecast> {
                return localDataSource.getForecast().map {
                    MapperEntityToDomain.mapForecastEntityToDomain(it)

                }

            }

            override fun shouldFetch(data: Forecast?): Boolean = true

            override suspend fun createCall(): Flow<ApiResponse<ForecastResponse>> =
                remoteDataSource.getForecast(lat, lon)

            override suspend fun saveCallResult(data: ForecastResponse) {
                val forecast = MapperResponseToEntity.mapForecastResponseToEntity(data)
                localDataSource.insertForecast(forecast)

            }
        }.asFlow()

    override fun getCurrentForecast(lat: Double, lon: Double): Flow<Resource<CurrentWeather>> =
        object : NetworkBoundResource<CurrentWeather, CurrentWeatherResponse>() {

            override fun loadFromDB(): Flow<CurrentWeather> {
                return localDataSource.getCurrentForecast().map {
                    MapperEntityToDomain.mapCurrentForecastEntityToDomain(it)
                }
            }

            override fun shouldFetch(data: CurrentWeather?): Boolean = true

            override suspend fun createCall(): Flow<ApiResponse<CurrentWeatherResponse>> =
                remoteDataSource.getCurrentForecast(lat, lon)

            override suspend fun saveCallResult(data: CurrentWeatherResponse) {
                val forecast = MapperResponseToEntity.mapCurrentForecastResponseToEntity(data)

                localDataSource.insertCurrentForecast(forecast)
            }
        }.asFlow()


    fun getAllUserData() = localDataSource.getAllUserData()

}