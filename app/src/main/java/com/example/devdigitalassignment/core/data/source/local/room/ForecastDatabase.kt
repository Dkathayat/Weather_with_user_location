package com.example.devdigitalassignment.core.data.source.local.room


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.devdigitalassignment.core.data.source.local.entity.UserWeatherTable
import com.kathayat.testWeather.core.data.source.local.entity.currentforecast.CurrentWeatherEntity
import com.example.devdigitalassignment.core.data.source.local.entity.forecast.ForecastEntity
import com.kathayat.testWeather.core.utils.DataConverter

@Database(
    entities = [ForecastEntity::class, CurrentWeatherEntity::class, UserWeatherTable::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(DataConverter::class)
abstract class ForecastDatabase: RoomDatabase() {

    abstract fun forecastDao(): ForecastDao

}