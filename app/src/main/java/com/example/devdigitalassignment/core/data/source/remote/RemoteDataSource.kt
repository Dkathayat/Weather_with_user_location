package com.kathayat.testWeather.core.data.source.remote


import android.util.Log
import com.kathayat.testWeather.core.data.source.remote.network.ApiClient
import com.kathayat.testWeather.core.data.source.remote.response.forecast.ForecastResponse
import com.kathayat.testWeather.core.data.source.remote.network.ApiResponse
import com.kathayat.testWeather.core.data.source.remote.response.currentforecast.CurrentWeatherResponse
import com.kathayat.testWeather.core.utils.Constant.API_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val service: ApiClient) {

    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(service: ApiClient): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource(service)
            }
    }

    private val units = "metric"

    private val appID: String
        get() = API_KEY

    suspend fun getForecast(lat: Double, lon: Double): Flow<ApiResponse<ForecastResponse>> {

        return flow {
            try {
                val response = service.getForecast(lat, lon, appID, units)

                if (response.list?.isNotEmpty() == true) {
                    emit(ApiResponse.Success(response))
                    Log.d("mainSharedViewModel",response.toString())
                } else {
                    emit(ApiResponse.Empty)
                }

            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCurrentForecast(lat: Double, lon: Double): Flow<ApiResponse<CurrentWeatherResponse>> {
        return flow {
            try {
                val response = service.getCurrentForecast(lat, lon, appID, units)

                emit(ApiResponse.Success(response))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

}