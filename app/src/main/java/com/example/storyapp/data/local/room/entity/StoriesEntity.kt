package com.example.storyapp.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "stories")
data class StoriesEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    @ColumnInfo(defaultValue = "0.0")
    val lon: Double,
    @ColumnInfo(defaultValue = "0.0")
    val lat: Double
)