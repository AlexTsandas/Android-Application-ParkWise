package com.example.parkingappproject.data

data class SavedParkingWithDetails(
    val parkingId: String,
    val name: String,
    val city: String,
    val area: String,
    val pricePerHour: Double,
    val note: String,
    val savedAt: Long,
    val reviewed: Int
)

