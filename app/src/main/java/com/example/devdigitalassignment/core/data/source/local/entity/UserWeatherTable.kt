package com.example.devdigitalassignment.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "all_user_entity")
data class UserWeatherTable(
    @PrimaryKey(autoGenerate = true)
    val id:Int?,
    val city:String,
    val temp:String
)