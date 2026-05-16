package com.example.parkingappproject.data

class FirestoreSeeder(
    private val repository: FirestoreRepository
) {

    suspend fun seedParkings() {
        val parkings = listOf(
            Parking("parking_1", "City Parking Stavroupoli", "Thessaloniki", "Stavroupoli", 2.00),
            Parking("parking_2", "Center Park", "Thessaloniki", "Stavroupoli Center", 1.50),
            Parking("parking_3", "Secure Parking 24h", "Thessaloniki", "Ano Ilioupoli", 2.50),
            Parking("parking_4", "West Side Parking", "Thessaloniki", "Evosmos", 2.20),

            Parking("parking_5", "Downtown Parking", "Thessaloniki", "Center", 2.80),
            Parking("parking_6", "White Tower Parking", "Thessaloniki", "Center", 3.00),
            Parking("parking_7", "Kalamaria Parking Point", "Thessaloniki", "Kalamaria", 2.40),
            Parking("parking_8", "Toumpa Easy Park", "Thessaloniki", "Toumpa", 1.80),
            Parking("parking_9", "Harilaou Parking Area", "Thessaloniki", "Harilaou", 1.70),
            Parking("parking_10", "Airport Parking Express", "Thessaloniki", "Airport", 4.00),
            Parking("parking_11", "Neapoli Parking Zone", "Thessaloniki", "Neapoli", 1.60),
            Parking("parking_12", "Polichni Safe Park", "Thessaloniki", "Polichni", 1.50),

            Parking("parking_13", "University Parking", "Thessaloniki", "Sindos", 1.20),
            Parking("parking_14", "Mall Parking One", "Thessaloniki", "Pylaia", 2.00),
            Parking("parking_15", "Mediterranean Cosmos Parking", "Thessaloniki", "Pylaia", 2.50),

            Parking("parking_16", "Paphos Center Parking", "Paphos", "Center", 1.50),
            Parking("parking_17", "Harbour Parking", "Paphos", "Kato Paphos", 2.00),
            Parking("parking_18", "Universal Parking Area", "Paphos", "Universal", 1.30),
            Parking("parking_19", "Limassol Marina Parking", "Limassol", "Marina", 3.00),
            Parking("parking_20", "Nicosia Central Parking", "Nicosia", "Center", 2.20)
        )

        repository.insertParkings(parkings)
    }

    suspend fun seedReviews() {
        val reviews = listOf(
            Triple("parking_1", "user_alex", "Clean parking and easy to access."),
            Triple("parking_1", "user_maria", "Good location, but it can get busy."),
            Triple("parking_1", "user_george", "Safe place and fair price."),

            Triple("parking_2", "user_maria", "Very useful if you want to park near the center."),
            Triple("parking_2", "user_xristos", "Small but convenient."),
            Triple("parking_2", "user_antonis", "Good price for the area."),

            Triple("parking_3", "user_alex", "Very secure parking with cameras."),
            Triple("parking_3", "user_xristos", "I felt safe leaving my car there."),
            Triple("parking_3", "user_george", "A bit expensive but worth it."),

            Triple("parking_4", "user_antonis", "Good option for Evosmos."),
            Triple("parking_4", "user_maria", "Easy to find and not too expensive."),

            Triple("parking_5", "user_alex", "Great location for downtown."),
            Triple("parking_5", "user_xristos", "Usually full during busy hours."),

            Triple("parking_6", "user_george", "Perfect if you are going near White Tower."),
            Triple("parking_6", "user_maria", "Expensive but very convenient."),

            Triple("parking_7", "user_antonis", "Good parking for Kalamaria."),
            Triple("parking_7", "user_xristos", "Clean and simple."),

            Triple("parking_8", "user_alex", "Easy parking near Toumpa."),
            Triple("parking_8", "user_george", "Good value for money."),

            Triple("parking_10", "user_maria", "Very useful for airport trips."),
            Triple("parking_10", "user_antonis", "Expensive but reliable."),

            Triple("parking_16", "user_alex", "Good parking in Paphos center."),
            Triple("parking_17", "user_maria", "Nice option near the harbour."),
            Triple("parking_19", "user_xristos", "Very convenient for Limassol Marina.")
        )

        reviews.forEachIndexed { index, item ->
            val parkingId = item.first
            val userId = item.second
            val comment = item.third

            val cleanliness = 3 + (index % 3)
            val security = 4 + (index % 2)
            val priceRating = 3 + (index % 3)
            val easyToFind = 4 + (index % 2)

            val calculatedRating =
                (cleanliness + security + priceRating + easyToFind) / 4.0

            val review = LocalReviewParking(
                parkingId = parkingId,
                cleanliness = cleanliness,
                security = security,
                priceRating = priceRating,
                easyToFind = easyToFind,
                calculatedRating = calculatedRating,
                comment = comment,
                createdAt = System.currentTimeMillis() + index
            )

            repository.insertReview(
                review = review,
                userId = userId
            )
        }
    }

    suspend fun seedSavedParkings() {
        val savedData = listOf(
            Triple("user_alex", "parking_1", "Parking κοντά στη σχολή / καθημερινή χρήση."),
            Triple("user_alex", "parking_3", "Καλό όταν θέλω πιο ασφαλές parking."),
            Triple("user_alex", "parking_7", "Να το θυμάμαι για Kalamaria."),

            Triple("user_maria", "parking_2", "Βολικό για δουλειές στο κέντρο."),
            Triple("user_maria", "parking_10", "Για όταν πάω airport."),

            Triple("user_george", "parking_4", "Κοντά στο σπίτι φίλου."),
            Triple("user_george", "parking_8", "Καλή επιλογή για Toumpa."),

            Triple("user_xristos", "parking_5", "Για βόλτα στο κέντρο."),
            Triple("user_xristos", "parking_19", "Για Limassol Marina."),

            Triple("user_antonis", "parking_6", "Για White Tower area."),
            Triple("user_antonis", "parking_15", "Για Cosmos.")
        )

        savedData.forEachIndexed { index, item ->
            val userId = item.first
            val parkingId = item.second
            val note = item.third

            val savedParking = SavedParking(
                parkingId = parkingId,
                note = note,
                savedAt = System.currentTimeMillis() + index
            )

            repository.saveParking(
                savedParking = savedParking,
                userId = userId
            )
        }
    }

    suspend fun seedAll() {
        seedParkings()
        seedReviews()
        seedSavedParkings()
    }
}
