package com.kathayat.testWeather.core.di

import android.content.Context
import androidx.room.Room
import com.example.devdigitalassignment.core.data.source.local.room.ForecastDao
import com.example.devdigitalassignment.core.data.source.local.room.ForecastDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): ForecastDatabase = Room.databaseBuilder(
        context,
        ForecastDatabase::class.java, "Tourism.db"
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideTourismDao(database: ForecastDatabase): ForecastDao = database.forecastDao()
}
