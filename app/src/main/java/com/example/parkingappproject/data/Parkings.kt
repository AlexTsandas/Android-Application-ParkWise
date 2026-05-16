package com.example.parkingappproject.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "parkings")
data class Parking(
    @PrimaryKey
    val parkingId: String,
    val name: String,
    val city: String,
    val area: String,
    val pricePerHour: Double
)

