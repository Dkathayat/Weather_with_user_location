package com.kathayat.testWeather.di

import com.kathayat.testWeather.core.domain.usecase.WeatherInteractor
import com.kathayat.testWeather.core.domain.usecase.WeatherUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppModule {

    @Binds
    @ViewModelScoped
    abstract fun provideWeatherUseCase(weatherInteractor: WeatherInteractor): WeatherUseCase

}