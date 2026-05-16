package com.example.parkingappproject.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        Parking::class,
        SavedParking::class,
        LocalReviewParking::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun parkingDao(): ParkingDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "parking_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}

