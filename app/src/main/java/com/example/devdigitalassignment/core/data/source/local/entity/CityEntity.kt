package com.example.devdigitalassignment.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import com.kathayat.testWeather.core.data.source.local.entity.CoordEntity

@Entity(tableName = "city")
data class CityEntity(

    @ColumnInfo(name = "cityId")
    val cityId: Int?,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "country")
    val country: String?,

    @ColumnInfo(name = "population")
    val population: String?,

    @ColumnInfo(name = "timezone")
    val timeZone: String?,

    @ColumnInfo(name = "sunrise")
    val sunrise: String?,

    @ColumnInfo(name = "sunset")
    val sunset: String?,

    @Embedded
    val coord: CoordEntity?
)
