package com.example.parkingappproject

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parkingappproject.data.Parking
import com.example.parkingappproject.data.ReviewWithParkingDetails
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun ParkingDetailsScreen(
    parking: Parking?,
    reviews: List<ReviewWithParkingDetails>,
    isSaved: Boolean,
    averageOverall: Double,
    averageCleanliness: Double,
    averageSecurity: Double,
    averagePrice: Double,
    averageEasyToFind: Double,
    onBackClick: () -> Unit,
    onSaveClick: (String) -> Unit,
    onLeaveReviewClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    var showNoteDialog by rememberSaveable { mutableStateOf(false) }
    var noteText by rememberSaveable { mutableStateOf("") }

    if (showNoteDialog) {
        AlertDialog(
            onDismissRequest = {
                showNoteDialog = false
                noteText = ""
            },
            title = {
                Text("Save parking")
            },
            text = {
                OutlinedTextField(
                    value = noteText,
                    onValueChange = {
                        if (it.length <= 120) {
                            noteText = it
                        }
                    },
                    label = { Text("Note") },
                    placeholder = { Text("Example: Parking near my gym") },
                    supportingText = {
                        Text("${noteText.length}/120")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color(0xFF2563EB),
                        unfocusedIndicatorColor = Color(0xFFCCCCCC),
                        focusedLabelColor = Color(0xFF2563EB),
                        unfocusedLabelColor = Color(0xFF777777),
                        cursorColor = Color(0xFF2563EB)
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onSaveClick(noteText)
                        showNoteDialog = false
                        noteText = ""
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2563EB),
                        contentColor = Color.White
                    )
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        showNoteDialog = false
                        noteText = ""
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFFF8E7)
    ) {
        if (parking == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Loading parking details...",
                    fontSize = 16.sp,
                    color = Color(0xFF777777)
                )
            }
        } else {
            if (isLandscape) {
                ParkingDetailsLandscapeContent(
                    parking = parking,
                    reviews = reviews,
                    isSaved = isSaved,
                    averageOverall = averageOverall,
                    averageCleanliness = averageCleanliness,
                    averageSecurity = averageSecurity,
                    averagePrice = averagePrice,
                    averageEasyToFind = averageEasyToFind,
                    onBackClick = onBackClick,
                    onSaveButtonClick = {
                        if (isSaved) {
                            onSaveClick("")
                        } else {
                            showNoteDialog = true
                        }
                    },
                    onLeaveReviewClick = onLeaveReviewClick
                )
            } else {
                ParkingDetailsPortraitContent(
                    parking = parking,
                    reviews = reviews,
                    isSaved = isSaved,
                    averageOverall = averageOverall,
                    averageCleanliness = averageCleanliness,
                    averageSecurity = averageSecurity,
                    averagePrice = averagePrice,
                    averageEasyToFind = averageEasyToFind,
                    onBackClick = onBackClick,
                    onSaveButtonClick = {
                        if (isSaved) {
                            onSaveClick("")
                        } else {
                            showNoteDialog = true
                        }
                    },
                    onLeaveReviewClick = onLeaveReviewClick
                )
            }
        }
    }
}

@Composable
fun ParkingDetailsPortraitContent(
    parking: Parking,
    reviews: List<ReviewWithParkingDetails>,
    isSaved: Boolean,
    averageOverall: Double,
    averageCleanliness: Double,
    averageSecurity: Double,
    averagePrice: Double,
    averageEasyToFind: Double,
    onBackClick: () -> Unit,
    onSaveButtonClick: () -> Unit,
    onLeaveReviewClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(id = R.drawable.back),
            contentDescription = "Back",
            modifier = Modifier
                .size(32.dp)
                .clickable {
                    onBackClick()
                }
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Parking Details",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E3A8A),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ParkingSummaryCard(
                    parking = parking,
                    averageOverall = averageOverall
                )
            }

            item {
                DetailedRatingsCard(
                    averageCleanliness = averageCleanliness,
                    averageSecurity = averageSecurity,
                    averagePrice = averagePrice,
                    averageEasyToFind = averageEasyToFind
                )
            }

            item {
                DetailActionButtons(
                    isSaved = isSaved,
                    onSaveButtonClick = onSaveButtonClick,
                    onLeaveReviewClick = onLeaveReviewClick
                )
            }

            item {
                Text(
                    text = "User Reviews",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E3A8A)
                )
            }

            if (reviews.isEmpty()) {
                item {
                    NoReviewsCard()
                }
            } else {
                items(reviews) { review ->
                    UserReviewCard(
                        userName = review.userName,
                        rating = "%.1f".format(review.calculatedRating),
                        comment = review.comment
                    )
                }
            }
        }
    }
}

@Composable
fun ParkingDetailsLandscapeContent(
    parking: Parking,
    reviews: List<ReviewWithParkingDetails>,
    isSaved: Boolean,
    averageOverall: Double,
    averageCleanliness: Double,
    averageSecurity: Double,
    averagePrice: Double,
    averageEasyToFind: Double,
    onBackClick: () -> Unit,
    onSaveButtonClick: () -> Unit,
    onLeaveReviewClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Image(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            onBackClick()
                        }
                )
            }

            item {
                Text(
                    text = "Parking Details",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E3A8A),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                ParkingSummaryCard(
                    parking = parking,
                    averageOverall = averageOverall
                )
            }

            item {
                DetailedRatingsCard(
                    averageCleanliness = averageCleanliness,
                    averageSecurity = averageSecurity,
                    averagePrice = averagePrice,
                    averageEasyToFind = averageEasyToFind
                )
            }

            item {
                DetailActionButtons(
                    isSaved = isSaved,
                    onSaveButtonClick = onSaveButtonClick,
                    onLeaveReviewClick = onLeaveReviewClick
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(6.dp))
            }

            item {
                Text(
                    text = "User Reviews",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E3A8A)
                )
            }

            if (reviews.isEmpty()) {
                item {
                    NoReviewsCard()
                }
            } else {
                items(reviews) { review ->
                    UserReviewCard(
                        userName = review.userName,
                        rating = "%.1f".format(review.calculatedRating),
                        comment = review.comment
                    )
                }
            }
        }
    }
}

@Composable
fun ParkingSummaryCard(
    parking: Parking,
    averageOverall: Double
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = parking.name,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E3A8A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${parking.city}, ${parking.area} • ${parking.pricePerHour}€/hour",
                fontSize = 15.sp,
                color = Color(0xFF555555)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (averageOverall == 0.0) {
                    "Average rating: No reviews yet"
                } else {
                    "Average rating: %.1f ★".format(averageOverall)
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF444444)
            )
        }
    }
}

@Composable
fun DetailedRatingsCard(
    averageCleanliness: Double,
    averageSecurity: Double,
    averagePrice: Double,
    averageEasyToFind: Double
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = "Detailed Ratings",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E3A8A)
            )

            Spacer(modifier = Modifier.height(12.dp))

            RatingDetailRow(label = "Cleanliness", value = averageCleanliness)
            RatingDetailRow(label = "Security", value = averageSecurity)
            RatingDetailRow(label = "Price satisfaction", value = averagePrice)
            RatingDetailRow(label = "Easy to find", value = averageEasyToFind)
        }
    }
}

@Composable
fun RatingDetailRow(
    label: String,
    value: Double
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 15.sp,
            color = Color(0xFF555555)
        )

        Text(
            text = if (value == 0.0) "No rating" else "%.1f ★".format(value),
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF444444)
        )
    }
}

@Composable
fun DetailActionButtons(
    isSaved: Boolean,
    onSaveButtonClick: () -> Unit,
    onLeaveReviewClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (isSaved) {
            Button(
                onClick = onSaveButtonClick,
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF59E0B),
                    contentColor = Color.White
                )
            ) {
                Text("Saved")
            }
        } else {
            OutlinedButton(
                onClick = onSaveButtonClick,
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF2563EB)
                )
            ) {
                Text("Save")
            }
        }

        Button(
            onClick = onLeaveReviewClick,
            modifier = Modifier
                .weight(1f)
                .height(44.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2563EB),
                contentColor = Color.White
            )
        ) {
            Text("Leave Review")
        }
    }
}

@Composable
fun UserReviewCard(
    userName: String,
    rating: String,
    comment: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = "$userName • $rating ★",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E3A8A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (comment.isBlank()) "No comment provided." else comment,
                fontSize = 15.sp,
                color = Color(0xFF555555)
            )
        }
    }
}

@Composable
fun NoReviewsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Text(
            text = "No reviews yet. Be the first to leave a review.",
            fontSize = 15.sp,
            color = Color(0xFF555555),
            modifier = Modifier.padding(18.dp)
        )
    }
}

