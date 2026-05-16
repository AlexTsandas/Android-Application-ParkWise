package com.example.parkingappproject

import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.parkingappproject.data.AppDatabase
import com.example.parkingappproject.data.FirestoreRepository
import com.example.parkingappproject.data.Parking
import com.example.parkingappproject.data.ReviewWithParkingDetails
import com.example.parkingappproject.data.SavedParking
import com.example.parkingappproject.data.SavedParkingWithDetails
import kotlinx.coroutines.launch
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var loggedInUserId by rememberSaveable { mutableStateOf("") }
    var loginError by rememberSaveable { mutableStateOf("") }

    fun encodeSearchText(text: String): String {
        val safeText = if (text.isBlank()) "__all__" else text
        return Uri.encode(safeText)
    }

    fun decodeSearchText(text: String?): String {
        val decoded = Uri.decode(text ?: "__all__")
        return if (decoded == "__all__") "" else decoded
    }

    fun navigateClean(route: String) {
        navController.navigate(route) {
            popUpTo("home") {
                inclusive = false
            }
            launchSingleTop = true
        }
    }

    suspend fun syncUserDataFromFirestoreToRoom(userId: String) {
        val database = AppDatabase.getDatabase(context)
        val dao = database.parkingDao()
        val firestoreRepository = FirestoreRepository()

        println("SYNC START FOR USER: $userId")

        dao.clearSavedParkings()
        dao.clearLocalReviews()

        val remoteSavedParkings = firestoreRepository.getSavedParkingsForUser(userId)
        val remoteReviews = firestoreRepository.getReviewsForUser(userId)

        println("REMOTE SAVED FOR $userId: $remoteSavedParkings")
        println("REMOTE REVIEWS FOR $userId: $remoteReviews")

        remoteSavedParkings.forEach { savedParking ->
            dao.saveParking(savedParking)
        }

        remoteReviews.forEach { review ->
            dao.insertReview(review)
        }

        val localSavedAfterSync = dao.getSavedParkingsWithDetails()
        val localReviewsAfterSync = dao.getReviewsWithParkingDetails()

        println("LOCAL SAVED AFTER SYNC: $localSavedAfterSync")
        println("LOCAL REVIEWS AFTER SYNC: $localReviewsAfterSync")
    }
    LaunchedEffect(loggedInUserId) {
        if (loggedInUserId.isNotBlank()) {
            syncUserDataFromFirestoreToRoom(loggedInUserId)
        }
    }

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                errorMessage = loginError,
                onLoginClick = { email, password ->
                    coroutineScope.launch {
                        val firestoreRepository = FirestoreRepository()

                        val user = firestoreRepository.loginUser(
                            email = email.trim(),
                            password = password.trim()
                        )

                        if (user != null) {
                            loggedInUserId = user.userId
                            loginError = ""

                            syncUserDataFromFirestoreToRoom(user.userId)

                            navController.navigate("home") {
                                popUpTo("login") {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        } else {
                            loginError = "Wrong email or password"
                        }
                    }
                }
            )
        }

        composable("home") {
            HomeScreen(
                onFindParkingClick = { searchText ->
                    navigateClean("parkingResults/${encodeSearchText(searchText)}")
                },
                onHistoryClick = {
                    navigateClean("history")
                },
                onLogoutClick = {
                    coroutineScope.launch {
                        val database = AppDatabase.getDatabase(context)
                        val dao = database.parkingDao()

                        dao.clearSavedParkings()
                        dao.clearLocalReviews()

                        loggedInUserId = ""

                        navController.navigate("login") {
                            popUpTo("home") {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        composable("parkingResults/{searchText}") { backStackEntry ->
            val searchText = decodeSearchText(
                backStackEntry.arguments?.getString("searchText")
            )

            var parkings by remember {
                mutableStateOf<List<Parking>>(emptyList())
            }

            var savedParkingIds by remember {
                mutableStateOf<Set<String>>(emptySet())
            }

            var parkingRatings by remember {
                mutableStateOf<Map<String, Double>>(emptyMap())
            }

            LaunchedEffect(searchText) {
                val database = AppDatabase.getDatabase(context)
                val dao = database.parkingDao()
                val firestoreRepository = FirestoreRepository()

                val cleanSearchText = searchText.trim()

                parkings = if (cleanSearchText.isBlank()) {
                    dao.getAllParkings()
                } else if (cleanSearchText.contains(",")) {
                    val parts = cleanSearchText.split(",").map { it.trim() }

                    val cityText = parts.getOrNull(0) ?: ""
                    val areaText = parts.getOrNull(1) ?: ""

                    if (cityText.isBlank()) {
                        dao.getAllParkings()
                    } else {
                        dao.searchParkingsByCityAndArea(
                            cityText = cityText,
                            areaText = areaText
                        )
                    }
                } else {
                    dao.searchParkings(cleanSearchText)
                }

                savedParkingIds = dao.getAllSavedParkings()
                    .map { it.parkingId }
                    .toSet()
                parkingRatings = parkings.associate { parking ->
                    val average = try {
                        firestoreRepository
                            .getRemoteRatingAveragesForParking(parking.parkingId)["overall"] ?: 0.0
                    } catch (e: Exception) {
                        dao.getAverageOverallRating(parking.parkingId) ?: 0.0
                    }

                    parking.parkingId to average
                }
            }

            ParkingResultsScreen(
                parkings = parkings,
                savedParkingIds = savedParkingIds,
                parkingRatings = parkingRatings,
                searchText = searchText,
                onSearchClick = { newSearchText ->
                    navigateClean("parkingResults/${encodeSearchText(newSearchText)}")
                },
                onBackClick = {
                    navigateClean("home")
                },
                onParkingClick = { parkingId ->
                    navigateClean(
                        "parkingDetailsFromResults/$parkingId/${encodeSearchText(searchText)}"
                    )
                },
                onLeaveReviewClick = { parkingId ->
                    navigateClean(
                        "addReviewFromResults/$parkingId/${encodeSearchText(searchText)}"
                    )
                },
                onSaveClick = { parking, note ->
                    coroutineScope.launch {
                        val database = AppDatabase.getDatabase(context)
                        val dao = database.parkingDao()
                        val firestoreRepository = FirestoreRepository()

                        if (savedParkingIds.contains(parking.parkingId)) {
                            dao.deleteSavedParking(parking.parkingId)
                            firestoreRepository.deleteSavedParking(parking.parkingId, loggedInUserId)
                        } else {
                            val savedParking = SavedParking(
                                parkingId = parking.parkingId,
                                note = note,
                                savedAt = System.currentTimeMillis()
                            )

                            dao.saveParking(savedParking)
                            firestoreRepository.saveParking(savedParking, loggedInUserId)

                            val existingReview = dao.getReviewForParking(parking.parkingId)

                            if (existingReview == null) {
                                NotificationHelper.showSaveNotification(
                                    context = context,
                                    parkingName = parking.name
                                )
                            }
                        }

                        savedParkingIds = dao.getAllSavedParkings()
                            .map { it.parkingId }
                            .toSet()
                    }
                }
            )
        }

        composable("parkingDetailsFromResults/{parkingId}/{searchText}") { backStackEntry ->
            val parkingId = backStackEntry.arguments?.getString("parkingId") ?: ""

            val searchText = decodeSearchText(
                backStackEntry.arguments?.getString("searchText")
            )

            var parking by remember {
                mutableStateOf<Parking?>(null)
            }

            var reviews by remember {
                mutableStateOf<List<ReviewWithParkingDetails>>(emptyList())
            }

            var isSaved by remember {
                mutableStateOf(false)
            }

            var averageOverall by remember {
                mutableStateOf(0.0)
            }

            var averageCleanliness by remember {
                mutableStateOf(0.0)
            }

            var averageSecurity by remember {
                mutableStateOf(0.0)
            }

            var averagePrice by remember {
                mutableStateOf(0.0)
            }

            var averageEasyToFind by remember {
                mutableStateOf(0.0)
            }

            LaunchedEffect(parkingId) {
                val database = AppDatabase.getDatabase(context)
                val dao = database.parkingDao()
                val firestoreRepository = FirestoreRepository()

                parking = dao.getParkingById(parkingId)

                reviews = firestoreRepository.getRemoteReviewsWithUserNamesForParking(parkingId)

                if (reviews.isEmpty()) {
                    reviews = dao.getReviewsForParking(parkingId)
                }

                isSaved = dao.getSavedParkingById(parkingId) != null

                val averages = firestoreRepository.getRemoteRatingAveragesForParking(parkingId)

                averageOverall = averages["overall"] ?: 0.0
                averageCleanliness = averages["cleanliness"] ?: 0.0
                averageSecurity = averages["security"] ?: 0.0
                averagePrice = averages["price"] ?: 0.0
                averageEasyToFind = averages["easyToFind"] ?: 0.0
            }

            ParkingDetailsScreen(
                parking = parking,
                reviews = reviews,
                isSaved = isSaved,
                averageOverall = averageOverall,
                averageCleanliness = averageCleanliness,
                averageSecurity = averageSecurity,
                averagePrice = averagePrice,
                averageEasyToFind = averageEasyToFind,
                onBackClick = {
                    navigateClean("parkingResults/${encodeSearchText(searchText)}")
                },
                onSaveClick = { note ->
                    coroutineScope.launch {
                        val database = AppDatabase.getDatabase(context)
                        val dao = database.parkingDao()
                        val firestoreRepository = FirestoreRepository()

                        if (isSaved) {
                            dao.deleteSavedParking(parkingId)
                            firestoreRepository.deleteSavedParking(
                                parkingId = parkingId,
                                userId = loggedInUserId
                            )
                            isSaved = false
                        } else {
                            val savedParking = SavedParking(
                                parkingId = parkingId,
                                note = note,
                                savedAt = System.currentTimeMillis()
                            )

                            dao.saveParking(savedParking)
                            firestoreRepository.saveParking(
                                savedParking = savedParking,
                                userId = loggedInUserId
                            )
                            isSaved = true

                            val existingReview = dao.getReviewForParking(parkingId)

                            if (existingReview == null) {
                                parking?.let { currentParking ->
                                    NotificationHelper.showSaveNotification(
                                        context = context,
                                        parkingName = currentParking.name
                                    )
                                }
                            }
                        }
                    }
                },
                onLeaveReviewClick = {
                    navigateClean(
                        "addReviewFromDetails/$parkingId/${encodeSearchText(searchText)}"
                    )
                }
            )
        }

        composable("parkingDetailsFromHistory/{parkingId}") { backStackEntry ->
            val parkingId = backStackEntry.arguments?.getString("parkingId") ?: ""

            var parking by remember {
                mutableStateOf<Parking?>(null)
            }

            var reviews by remember {
                mutableStateOf<List<ReviewWithParkingDetails>>(emptyList())
            }

            var isSaved by remember {
                mutableStateOf(false)
            }

            var averageOverall by remember {
                mutableStateOf(0.0)
            }

            var averageCleanliness by remember {
                mutableStateOf(0.0)
            }

            var averageSecurity by remember {
                mutableStateOf(0.0)
            }

            var averagePrice by remember {
                mutableStateOf(0.0)
            }

            var averageEasyToFind by remember {
                mutableStateOf(0.0)
            }

            LaunchedEffect(parkingId) {
                val database = AppDatabase.getDatabase(context)
                val dao = database.parkingDao()
                val firestoreRepository = FirestoreRepository()

                parking = dao.getParkingById(parkingId)

                reviews = firestoreRepository.getRemoteReviewsWithUserNamesForParking(parkingId)

                if (reviews.isEmpty()) {
                    reviews = dao.getReviewsForParking(parkingId)
                }

                isSaved = dao.getSavedParkingById(parkingId) != null

                val averages = firestoreRepository.getRemoteRatingAveragesForParking(parkingId)

                averageOverall = averages["overall"] ?: 0.0
                averageCleanliness = averages["cleanliness"] ?: 0.0
                averageSecurity = averages["security"] ?: 0.0
                averagePrice = averages["price"] ?: 0.0
                averageEasyToFind = averages["easyToFind"] ?: 0.0
            }

            ParkingDetailsScreen(
                parking = parking,
                reviews = reviews,
                isSaved = isSaved,
                averageOverall = averageOverall,
                averageCleanliness = averageCleanliness,
                averageSecurity = averageSecurity,
                averagePrice = averagePrice,
                averageEasyToFind = averageEasyToFind,
                onBackClick = {
                    navigateClean("history")
                },
                onSaveClick = { note ->
                    coroutineScope.launch {
                        val database = AppDatabase.getDatabase(context)
                        val dao = database.parkingDao()
                        val firestoreRepository = FirestoreRepository()

                        if (isSaved) {
                            dao.deleteSavedParking(parkingId)
                            firestoreRepository.deleteSavedParking(
                                parkingId = parkingId,
                                userId = loggedInUserId
                            )
                            isSaved = false
                        } else {
                            val savedParking = SavedParking(
                                parkingId = parkingId,
                                note = note,
                                savedAt = System.currentTimeMillis()
                            )

                            dao.saveParking(savedParking)
                            firestoreRepository.saveParking(
                                savedParking = savedParking,
                                userId = loggedInUserId
                            )
                            isSaved = true

                            val existingReview = dao.getReviewForParking(parkingId)

                            if (existingReview == null) {
                                parking?.let { currentParking ->
                                    NotificationHelper.showSaveNotification(
                                        context = context,
                                        parkingName = currentParking.name
                                    )
                                }
                            }
                        }
                    }
                },
                onLeaveReviewClick = {
                    navigateClean("addReviewFromHistory/$parkingId")
                }
            )
        }

        composable("history") {
            var savedParkings by remember {
                mutableStateOf<List<SavedParkingWithDetails>>(emptyList())
            }

            var reviews by remember {
                mutableStateOf<List<ReviewWithParkingDetails>>(emptyList())
            }

            LaunchedEffect(Unit) {
                val database = AppDatabase.getDatabase(context)
                val dao = database.parkingDao()

                savedParkings = dao.getSavedParkingsWithDetails()
                reviews = dao.getReviewsWithParkingDetails()
            }

            MyParkingHistoryScreen(
                savedParkings = savedParkings,
                reviews = reviews,
                onBackClick = {
                    navigateClean("home")
                },
                onParkingClick = { parkingId ->
                    navigateClean("parkingDetailsFromHistory/$parkingId")
                },
                onLeaveReviewClick = { parkingId ->
                    navigateClean("addReviewFromHistory/$parkingId")
                },
                onEditReviewClick = { parkingId ->
                    navigateClean("addReviewFromHistory/$parkingId")
                },
                onDeleteReviewClick = { parkingId ->
                    coroutineScope.launch {
                        val database = AppDatabase.getDatabase(context)
                        val dao = database.parkingDao()
                        val firestoreRepository = FirestoreRepository()

                        dao.deleteReview(parkingId)
                        firestoreRepository.deleteReview(
                            parkingId = parkingId,
                            userId = loggedInUserId
                        )

                        savedParkings = dao.getSavedParkingsWithDetails()
                        reviews = dao.getReviewsWithParkingDetails()
                    }
                },
                onUnsaveClick = { parkingId ->
                    coroutineScope.launch {
                        val database = AppDatabase.getDatabase(context)
                        val dao = database.parkingDao()
                        val firestoreRepository = FirestoreRepository()

                        dao.deleteSavedParking(parkingId)
                        firestoreRepository.deleteSavedParking(
                            parkingId = parkingId,
                            userId = loggedInUserId
                        )

                        savedParkings = dao.getSavedParkingsWithDetails()
                        reviews = dao.getReviewsWithParkingDetails()
                    }
                }
            )
        }

        composable("addReviewFromResults/{parkingId}/{searchText}") { backStackEntry ->
            val parkingId = backStackEntry.arguments?.getString("parkingId") ?: ""

            val searchText = decodeSearchText(
                backStackEntry.arguments?.getString("searchText")
            )

            AddReviewScreen(
                parkingId = parkingId,
                onBackClick = {
                    navigateClean("parkingResults/${encodeSearchText(searchText)}")
                },
                onSubmitClick = { review ->
                    coroutineScope.launch {
                        val database = AppDatabase.getDatabase(context)
                        val dao = database.parkingDao()
                        val firestoreRepository = FirestoreRepository()

                        dao.insertReview(review)
                        firestoreRepository.insertReview(
                            review = review,
                            userId = loggedInUserId
                        )

                        Toast.makeText(
                            context,
                            "Thank you for your feedback",
                            Toast.LENGTH_SHORT
                        ).show()

                        navigateClean("parkingResults/${encodeSearchText(searchText)}")
                    }
                }
            )
        }

        composable("addReviewFromHistory/{parkingId}") { backStackEntry ->
            val parkingId = backStackEntry.arguments?.getString("parkingId") ?: ""

            AddReviewScreen(
                parkingId = parkingId,
                onBackClick = {
                    navigateClean("history")
                },
                onSubmitClick = { review ->
                    coroutineScope.launch {
                        val database = AppDatabase.getDatabase(context)
                        val dao = database.parkingDao()
                        val firestoreRepository = FirestoreRepository()

                        dao.insertReview(review)
                        firestoreRepository.insertReview(
                            review = review,
                            userId = loggedInUserId
                        )

                        Toast.makeText(
                            context,
                            "Thank you for your feedback",
                            Toast.LENGTH_SHORT
                        ).show()

                        navigateClean("history")
                    }
                }
            )
        }

        composable("addReviewFromDetails/{parkingId}/{searchText}") { backStackEntry ->
            val parkingId = backStackEntry.arguments?.getString("parkingId") ?: ""

            val searchText = decodeSearchText(
                backStackEntry.arguments?.getString("searchText")
            )

            AddReviewScreen(
                parkingId = parkingId,
                onBackClick = {
                    navigateClean(
                        "parkingDetailsFromResults/$parkingId/${encodeSearchText(searchText)}"
                    )
                },
                onSubmitClick = { review ->
                    coroutineScope.launch {
                        val database = AppDatabase.getDatabase(context)
                        val dao = database.parkingDao()
                        val firestoreRepository = FirestoreRepository()

                        dao.insertReview(review)
                        firestoreRepository.insertReview(
                            review = review,
                            userId = loggedInUserId
                        )

                        Toast.makeText(
                            context,
                            "Thank you for your feedback",
                            Toast.LENGTH_SHORT
                        ).show()

                        navigateClean(
                            "parkingDetailsFromResults/$parkingId/${encodeSearchText(searchText)}"
                        )
                    }
                }
            )
        }
    }
}

