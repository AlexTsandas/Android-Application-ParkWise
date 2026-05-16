package com.example.parkingappproject.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.example.parkingappproject.data.ReviewWithParkingDetails

class FirestoreRepository {

    private val firestore = FirebaseFirestore.getInstance()

    // -------------------------
    // PARKINGS
    // -------------------------

    suspend fun insertParking(parking: Parking) {
        firestore
            .collection("parkings")
            .document(parking.parkingId)
            .set(
                hashMapOf(
                    "parkingId" to parking.parkingId,
                    "name" to parking.name,
                    "city" to parking.city,
                    "area" to parking.area,
                    "pricePerHour" to parking.pricePerHour
                )
            )
            .await()
    }

    suspend fun insertParkings(parkings: List<Parking>) {
        for (parking in parkings) {
            insertParking(parking)
        }
    }

    suspend fun getRemoteParkingsByCity(city: String): List<Map<String, Any>> {
        val result = firestore
            .collection("parkings")
            .whereEqualTo("city", city)
            .get()
            .await()

        return result.documents.mapNotNull { it.data }
    }

    // -------------------------
    // SAVED PARKINGS
    // -------------------------

    suspend fun saveParking(
        savedParking: SavedParking,
        userId: String
    ) {
        val documentId = "${userId}_${savedParking.parkingId}"

        firestore
            .collection("saved_parkings")
            .document(documentId)
            .set(
                hashMapOf(
                    "userId" to userId,
                    "parkingId" to savedParking.parkingId,
                    "note" to savedParking.note,
                    "savedAt" to savedParking.savedAt
                )
            )
            .await()
    }

    suspend fun deleteSavedParking(
        parkingId: String,
        userId: String
    ) {
        val documentId = "${userId}_$parkingId"

        firestore
            .collection("saved_parkings")
            .document(documentId)
            .delete()
            .await()
    }

    suspend fun getRemoteSavedParkings(
        userId: String
    ): List<Map<String, Any>> {
        val result = firestore
            .collection("saved_parkings")
            .whereEqualTo("userId", userId)
            .get()
            .await()

        return result.documents.mapNotNull { it.data }
    }

    // -------------------------
    // REVIEWS
    // -------------------------

    suspend fun insertReview(
        review: LocalReviewParking,
        userId: String
    ) {
        val documentId = "${userId}_${review.parkingId}"

        firestore
            .collection("reviews")
            .document(documentId)
            .set(
                hashMapOf(
                    "userId" to userId,
                    "parkingId" to review.parkingId,
                    "cleanliness" to review.cleanliness,
                    "security" to review.security,
                    "priceRating" to review.priceRating,
                    "easyToFind" to review.easyToFind,
                    "calculatedRating" to review.calculatedRating,
                    "comment" to review.comment,
                    "createdAt" to review.createdAt
                )
            )
            .await()
    }

    suspend fun updateReview(
        review: LocalReviewParking,
        userId: String
    ) {
        val documentId = "${userId}_${review.parkingId}"

        firestore
            .collection("reviews")
            .document(documentId)
            .set(
                hashMapOf(
                    "userId" to userId,
                    "parkingId" to review.parkingId,
                    "cleanliness" to review.cleanliness,
                    "security" to review.security,
                    "priceRating" to review.priceRating,
                    "easyToFind" to review.easyToFind,
                    "calculatedRating" to review.calculatedRating,
                    "comment" to review.comment,
                    "createdAt" to review.createdAt
                )
            )
            .await()
    }

    suspend fun deleteReview(
        parkingId: String,
        userId: String
    ) {
        val documentId = "${userId}_$parkingId"

        firestore
            .collection("reviews")
            .document(documentId)
            .delete()
            .await()
    }

    // Reviews που έκανε συγκεκριμένος user
    suspend fun getRemoteReviewsForUser(
        userId: String
    ): List<Map<String, Any>> {
        val result = firestore
            .collection("reviews")
            .whereEqualTo("userId", userId)
            .get()
            .await()

        return result.documents.mapNotNull { it.data }
    }

    // Reviews για συγκεκριμένο parking από όλους τους users
    suspend fun getRemoteReviewsForParking(
        parkingId: String
    ): List<Map<String, Any>> {
        val result = firestore
            .collection("reviews")
            .whereEqualTo("parkingId", parkingId)
            .get()
            .await()

        return result.documents.mapNotNull { it.data }
    }

    // -------------------------
    // LOGIN
    // -------------------------

    suspend fun loginUser(
        email: String,
        password: String
    ): AppUser? {
        val result = firestore
            .collection("Users")
            .whereEqualTo("email", email)
            .whereEqualTo("password", password)
            .get()
            .await()

        val document = result.documents.firstOrNull()

        return document?.toObject(AppUser::class.java)
    }

    suspend fun getSavedParkingsForUser(userId: String): List<SavedParking> {
        val result = firestore
            .collection("saved_parkings")
            .whereEqualTo("userId", userId)
            .get()
            .await()

        return result.documents.mapNotNull { document ->
            val parkingId = document.getString("parkingId") ?: return@mapNotNull null
            val note = document.getString("note") ?: ""
            val savedAt = document.getLong("savedAt") ?: System.currentTimeMillis()

            SavedParking(
                parkingId = parkingId,
                note = note,
                savedAt = savedAt
            )
        }
    }

    suspend fun getReviewsForUser(userId: String): List<LocalReviewParking> {
        val result = firestore
            .collection("reviews")
            .whereEqualTo("userId", userId)
            .get()
            .await()

        return result.documents.mapNotNull { document ->
            val parkingId = document.getString("parkingId") ?: return@mapNotNull null

            LocalReviewParking(
                parkingId = parkingId,
                cleanliness = document.getLong("cleanliness")?.toInt() ?: 0,
                security = document.getLong("security")?.toInt() ?: 0,
                priceRating = document.getLong("priceRating")?.toInt() ?: 0,
                easyToFind = document.getLong("easyToFind")?.toInt() ?: 0,
                calculatedRating = document.getDouble("calculatedRating") ?: 0.0,
                comment = document.getString("comment") ?: "",
                createdAt = document.getLong("createdAt") ?: System.currentTimeMillis()
            )
        }
    }

    suspend fun getUserDisplayName(userId: String): String {
        val result = firestore
            .collection("Users")
            .whereEqualTo("userId", userId)
            .get()
            .await()

        val document = result.documents.firstOrNull()

        val email = document?.getString("email")

        return if (!email.isNullOrBlank()) {
            email.substringBefore("@")
        } else {
            userId
        }
    }

    suspend fun getRemoteReviewsWithUserNamesForParking(
        parkingId: String
    ): List<ReviewWithParkingDetails> {
        val result = firestore
            .collection("reviews")
            .whereEqualTo("parkingId", parkingId)
            .get()
            .await()

        return result.documents.map { document ->
            val userId = document.getString("userId") ?: "unknown_user"
            val userName = getUserDisplayName(userId)

            ReviewWithParkingDetails(
                parkingId = document.getString("parkingId") ?: "",
                parkingName = "",
                userName = userName,
                calculatedRating = document.getDouble("calculatedRating") ?: 0.0,
                comment = document.getString("comment") ?: "",
                createdAt = document.getLong("createdAt") ?: 0L
            )
        }
    }

    suspend fun getRemoteRatingAveragesForParking(parkingId: String): Map<String, Double> {
        val result = firestore
            .collection("reviews")
            .whereEqualTo("parkingId", parkingId)
            .get()
            .await()

        val documents = result.documents

        if (documents.isEmpty()) {
            return mapOf(
                "overall" to 0.0,
                "cleanliness" to 0.0,
                "security" to 0.0,
                "price" to 0.0,
                "easyToFind" to 0.0
            )
        }

        val overallAverage = documents.map { it.getDouble("calculatedRating") ?: 0.0 }.average()
        val cleanlinessAverage = documents.map { it.getDouble("cleanliness") ?: 0.0 }.average()
        val securityAverage = documents.map { it.getDouble("security") ?: 0.0 }.average()
        val priceAverage = documents.map { it.getDouble("priceRating") ?: 0.0 }.average()
        val easyToFindAverage = documents.map { it.getDouble("easyToFind") ?: 0.0 }.average()

        return mapOf(
            "overall" to overallAverage,
            "cleanliness" to cleanlinessAverage,
            "security" to securityAverage,
            "price" to priceAverage,
            "easyToFind" to easyToFindAverage
        )
    }

    suspend fun getAllRemoteParkings(): List<Parking> {
        val result = firestore
            .collection("parkings")
            .get()
            .await()

        return result.documents.mapNotNull { document ->
            val parkingId = document.getString("parkingId") ?: return@mapNotNull null
            val name = document.getString("name") ?: return@mapNotNull null
            val city = document.getString("city") ?: return@mapNotNull null
            val area = document.getString("area") ?: return@mapNotNull null
            val pricePerHour = document.getDouble("pricePerHour") ?: 0.0

            Parking(
                parkingId = parkingId,
                name = name,
                city = city,
                area = area,
                pricePerHour = pricePerHour
            )
        }
    }

}

