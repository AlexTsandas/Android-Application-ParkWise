package com.example.parkingappproject.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "local_review_parkings",
    foreignKeys = [
        ForeignKey(
            entity = Parking::class,
            parentColumns = ["parkingId"],
            childColumns = ["parkingId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class LocalReviewParking(
    @PrimaryKey
    val parkingId: String,
    val cleanliness: Int,
    val security: Int,
    val priceRating: Int,
    val easyToFind: Int,
    val calculatedRating: Double,
    val comment: String,
    val createdAt: Long
)

