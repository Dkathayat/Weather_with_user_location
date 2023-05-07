package com.example.devdigitalassignment.core.di

import com.example.devdigitalassignment.core.data.WeatherRepository
import com.kathayat.testWeather.core.di.DatabaseModule
import com.kathayat.testWeather.core.di.NetworkModule
import com.kathayat.testWeather.core.domain.repository.IWeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [NetworkModule::class, DatabaseModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideRepository(tourismRepository: WeatherRepository): IWeatherRepository

}