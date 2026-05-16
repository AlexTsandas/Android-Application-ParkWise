package com.example.parkingappproject.data

data class ReviewWithParkingDetails(
    val parkingId: String,
    val parkingName: String,
    val userName: String,
    val calculatedRating: Double,
    val comment: String,
    val createdAt: Long
)

