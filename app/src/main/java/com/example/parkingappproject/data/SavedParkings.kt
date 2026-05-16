package com.example.parkingappproject.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "saved_parkings",
    foreignKeys = [
        ForeignKey(
            entity = Parking::class,
            parentColumns = ["parkingId"],
            childColumns = ["parkingId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SavedParking(
    @PrimaryKey
    val parkingId: String,
    val note: String,
    val savedAt: Long
)

