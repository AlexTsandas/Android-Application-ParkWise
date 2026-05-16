package com.example.parkingappproject.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ParkingDao {



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParking(parking: Parking)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParkings(parkings: List<Parking>)

    @Query("SELECT * FROM parkings")
    suspend fun getAllParkings(): List<Parking>

    @Query(
        """
    SELECT * FROM parkings
    WHERE city LIKE '%' || :searchText || '%'
       OR area LIKE '%' || :searchText || '%'
       OR name LIKE '%' || :searchText || '%'
    """
    )
    suspend fun searchParkings(searchText: String): List<Parking>

    @Query(
        """
    SELECT * FROM parkings
    WHERE 
        city LIKE '%' || :cityText || '%'
    AND
        (
            :areaText = ''
            OR area LIKE '%' || :areaText || '%'
            OR name LIKE '%' || :areaText || '%'
        )
    """
    )
    suspend fun searchParkingsByCityAndArea(
        cityText: String,
        areaText: String
    ): List<Parking>

    @Query("SELECT * FROM parkings WHERE parkingId = :parkingId")
    suspend fun getParkingById(parkingId: String): Parking?

    @Update
    suspend fun updateParking(parking: Parking)

    @Delete
    suspend fun deleteParking(parking: Parking)




    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveParking(savedParking: SavedParking)

    @Query("DELETE FROM saved_parkings")
    suspend fun clearSavedParkings()

    @Query("DELETE FROM local_review_parkings")
    suspend fun clearLocalReviews()

    @Query("SELECT * FROM saved_parkings")
    suspend fun getAllSavedParkings(): List<SavedParking>
    @Query(
        """
    SELECT 
        p.parkingId AS parkingId,
        p.name AS name,
        p.city AS city,
        p.area AS area,
        p.pricePerHour AS pricePerHour,
        s.note AS note,
        s.savedAt AS savedAt,
        CASE 
            WHEN r.parkingId IS NULL THEN 0 
            ELSE 1 
        END AS reviewed
    FROM saved_parkings s
    INNER JOIN parkings p ON s.parkingId = p.parkingId
    LEFT JOIN local_review_parkings r ON s.parkingId = r.parkingId
    """
    )
    suspend fun getSavedParkingsWithDetails(): List<SavedParkingWithDetails>



    @Query("DELETE FROM saved_parkings WHERE parkingId = :parkingId")
    suspend fun deleteSavedParking(parkingId: String)




    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: LocalReviewParking)

    @Query(
        """
SELECT 
    r.parkingId AS parkingId,
    p.name AS parkingName,
    'Current User' AS userName,
    r.calculatedRating AS calculatedRating,
    r.comment AS comment,
    r.createdAt AS createdAt
FROM local_review_parkings r
INNER JOIN parkings p ON r.parkingId = p.parkingId
"""
    )
    suspend fun getReviewsWithParkingDetails(): List<ReviewWithParkingDetails>
    @Query("SELECT * FROM local_review_parkings")
    suspend fun getAllLocalReviews(): List<LocalReviewParking>

    @Query("SELECT * FROM local_review_parkings WHERE parkingId = :parkingId")
    suspend fun getReviewForParking(parkingId: String): LocalReviewParking?

    @Update
    suspend fun updateReview(review: LocalReviewParking)

    @Query("DELETE FROM local_review_parkings WHERE parkingId = :parkingId")
    suspend fun deleteReview(parkingId: String)

    @Query("SELECT * FROM saved_parkings WHERE parkingId = :parkingId LIMIT 1")
    suspend fun getSavedParkingById(parkingId: String): SavedParking?

    @Query(
        """
SELECT 
    r.parkingId AS parkingId,
    p.name AS parkingName,
    'Current User' AS userName,
    r.calculatedRating AS calculatedRating,
    r.comment AS comment,
    r.createdAt AS createdAt
FROM local_review_parkings r
INNER JOIN parkings p ON r.parkingId = p.parkingId
WHERE r.parkingId = :parkingId
"""
    )
    suspend fun getReviewsForParking(parkingId: String): List<ReviewWithParkingDetails>



    @Query(
        """
    SELECT AVG(cleanliness) 
    FROM local_review_parkings 
    WHERE parkingId = :parkingId
    """
    )
    suspend fun getAverageCleanliness(parkingId: String): Double?

    @Query(
        """
    SELECT AVG(easyToFind) 
    FROM local_review_parkings 
    WHERE parkingId = :parkingId
    """
    )
    suspend fun getAverageEasyToFind(parkingId: String): Double?

    @Query(
        """
    SELECT AVG(priceRating) 
    FROM local_review_parkings 
    WHERE parkingId = :parkingId
    """
    )
    suspend fun getAveragePriceRating(parkingId: String): Double?

    @Query(
        """
    SELECT AVG(security) 
    FROM local_review_parkings 
    WHERE parkingId = :parkingId
    """
    )
    suspend fun getAverageSecurity(parkingId: String): Double?

    @Query(
        """
    SELECT AVG(calculatedRating) 
    FROM local_review_parkings 
    WHERE parkingId = :parkingId
    """
    )
    suspend fun getAverageOverallRating(parkingId: String): Double?
}

