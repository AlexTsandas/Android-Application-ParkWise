package com.example.parkingappproject

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import com.example.parkingappproject.data.AppDatabase
import com.example.parkingappproject.data.FirestoreRepository
import com.example.parkingappproject.data.Parking
import com.example.parkingappproject.ui.theme.ParkingAppProjectTheme
import com.example.parkingappproject.data.FirestoreSeeder
class MainActivity : ComponentActivity() {

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        setContent {
            ParkingAppProjectTheme {

                LaunchedEffect(Unit) {
                    val database = AppDatabase.getDatabase(applicationContext)
                    val dao = database.parkingDao()
                    val firestoreRepository = FirestoreRepository()

                    //val seeder = FirestoreSeeder(firestoreRepository)
                   // seeder.seedAll()

                    val remoteParkings = firestoreRepository.getAllRemoteParkings()

                    if (remoteParkings.isNotEmpty()) {
                        dao.insertParkings(remoteParkings)
                        println("PARKINGS SYNCED FROM FIRESTORE TO ROOM: $remoteParkings")
                    } else {
                        val initialParkings = listOf(
                            Parking(
                                parkingId = "parking_1",
                                name = "City Parking Stavroupoli",
                                city = "Thessaloniki",
                                area = "Stavroupoli",
                                pricePerHour = 2.00
                            ),
                            Parking(
                                parkingId = "parking_2",
                                name = "Center Park",
                                city = "Thessaloniki",
                                area = "Stavroupoli Center",
                                pricePerHour = 1.50
                            ),
                            Parking(
                                parkingId = "parking_3",
                                name = "Secure Parking 24h",
                                city = "Thessaloniki",
                                area = "Ano Ilioupoli",
                                pricePerHour = 2.50
                            ),
                            Parking(
                                parkingId = "parking_4",
                                name = "West Side Parking",
                                city = "Thessaloniki",
                                area = "Evosmos",
                                pricePerHour = 2.20
                            )
                        )

                        dao.insertParkings(initialParkings)
                        println("FALLBACK PARKINGS INSERTED LOCALLY")
                    }
                }

                AppNavigation()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ParkingAppProjectTheme {

    }
}