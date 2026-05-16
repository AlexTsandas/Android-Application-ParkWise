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
import com.example.parkingappproject.data.ReviewWithParkingDetails
import com.example.parkingappproject.data.SavedParkingWithDetails
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun MyParkingHistoryScreen(
    savedParkings: List<SavedParkingWithDetails>,
    reviews: List<ReviewWithParkingDetails>,
    onBackClick: () -> Unit,
    onParkingClick: (String) -> Unit,
    onLeaveReviewClick: (String) -> Unit,
    onEditReviewClick: (String) -> Unit,
    onUnsaveClick: (String) -> Unit,
    onDeleteReviewClick: (String) -> Unit
) {
    var selectedTab by rememberSaveable { mutableStateOf("Saved") }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFFF8E7)
    ) {
        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .width(260.dp)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = "Back to Home",
                        modifier = Modifier
                            .size(32.dp)
                            .clickable {
                                onBackClick()
                            }
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    Text(
                        text = "My Parking\nHistory",
                        fontSize = 30.sp,
                        lineHeight = 34.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E3A8A)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Saved parkings and your reviews.",
                        fontSize = 15.sp,
                        color = Color(0xFF555555)
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    HistoryTabButton(
                        text = "Saved Parkings",
                        selected = selectedTab == "Saved",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        onClick = { selectedTab = "Saved" }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    HistoryTabButton(
                        text = "My Reviews",
                        selected = selectedTab == "Reviews",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        onClick = { selectedTab = "Reviews" }
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    Text(
                        text = if (selectedTab == "Saved") "Saved Parkings" else "My Reviews",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E3A8A)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    if (selectedTab == "Saved") {
                        SavedParkingsContent(
                            savedParkings = savedParkings,
                            modifier = Modifier.fillMaxSize(),
                            onParkingClick = onParkingClick,
                            onLeaveReviewClick = onLeaveReviewClick,
                            onUnsaveClick = onUnsaveClick
                        )
                    } else {
                        MyReviewsContent(
                            reviews = reviews,
                            modifier = Modifier.fillMaxSize(),
                            onEditReviewClick = onEditReviewClick,
                            onDeleteReviewClick = onDeleteReviewClick
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                Image(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "Back to Home",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            onBackClick()
                        }
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "My Parking History",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E3A8A),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HistoryTabButton(
                        text = "Saved Parkings",
                        selected = selectedTab == "Saved",
                        modifier = Modifier.weight(1f),
                        onClick = { selectedTab = "Saved" }
                    )

                    HistoryTabButton(
                        text = "My Reviews",
                        selected = selectedTab == "Reviews",
                        modifier = Modifier.weight(1f),
                        onClick = { selectedTab = "Reviews" }
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                if (selectedTab == "Saved") {
                    SavedParkingsContent(
                        savedParkings = savedParkings,
                        modifier = Modifier.weight(1f),
                        onParkingClick = onParkingClick,
                        onLeaveReviewClick = onLeaveReviewClick,
                        onUnsaveClick = onUnsaveClick
                    )
                } else {
                    MyReviewsContent(
                        reviews = reviews,
                        modifier = Modifier.weight(1f),
                        onEditReviewClick = onEditReviewClick,
                        onDeleteReviewClick = onDeleteReviewClick
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryTabButton(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        color = if (selected) Color(0xFF2563EB) else Color.White,
        shadowElevation = if (selected) 4.dp else 1.dp
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (selected) Color.White else Color(0xFF1E3A8A)
            )
        }
    }
}

@Composable
fun SavedParkingsContent(
    savedParkings: List<SavedParkingWithDetails>,
    modifier: Modifier = Modifier,
    onParkingClick: (String) -> Unit,
    onLeaveReviewClick: (String) -> Unit,
    onUnsaveClick: (String) -> Unit
) {
    if (savedParkings.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No saved parkings yet.",
                fontSize = 16.sp,
                color = Color(0xFF777777)
            )
        }
    } else {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            items(savedParkings) { savedParking ->
                HistoryParkingCard(
                    parkingId = savedParking.parkingId,
                    name = savedParking.name,
                    area = "${savedParking.city}, ${savedParking.area}",
                    price = "${savedParking.pricePerHour}€/hour",
                    rating = "4.4",
                    note = savedParking.note,
                    reviewed = savedParking.reviewed == 1,
                    onCardClick = {
                        onParkingClick(savedParking.parkingId)
                    },
                    onLeaveReviewClick = onLeaveReviewClick,
                    onUnsaveClick = onUnsaveClick
                )
            }
        }
    }
}

@Composable
fun HistoryParkingCard(
    parkingId: String,
    name: String,
    area: String,
    price: String,
    rating: String,
    note: String,
    reviewed: Boolean,
    onCardClick: () -> Unit,
    onLeaveReviewClick: (String) -> Unit,
    onUnsaveClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onCardClick()
            },
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
                text = name,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E3A8A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$area • $price",
                fontSize = 15.sp,
                color = Color(0xFF555555)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Rating: $rating ★",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF444444)
            )

            if (note.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFFF3C4)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = "Personal note",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF92400E)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = note,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF333333)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (reviewed) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "✓ Reviewed",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF16A34A)
                    )

                    Button(
                        onClick = {
                            onUnsaveClick(parkingId)
                        },
                        modifier = Modifier.height(42.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF59E0B),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Saved")
                    }
                }
            } else {
                Text(
                    text = "Not reviewed yet",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFD97706)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            onLeaveReviewClick(parkingId)
                        },
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

                    Button(
                        onClick = {
                            onUnsaveClick(parkingId)
                        },
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
                }
            }
        }
    }
}

@Composable
fun MyReviewsContent(
    reviews: List<ReviewWithParkingDetails>,
    modifier: Modifier = Modifier,
    onEditReviewClick: (String) -> Unit,
    onDeleteReviewClick: (String) -> Unit
) {
    if (reviews.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No reviews yet.",
                fontSize = 16.sp,
                color = Color(0xFF777777)
            )
        }
    } else {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            items(reviews) { review ->
                HistoryReviewCard(
                    parkingId = review.parkingId,
                    parkingName = review.parkingName,
                    rating = "%.1f".format(review.calculatedRating),
                    comment = review.comment,
                    onEditClick = onEditReviewClick,
                    onDeleteClick = onDeleteReviewClick
                )
            }
        }
    }
}

@Composable
fun HistoryReviewCard(
    parkingId: String,
    parkingName: String,
    rating: String,
    comment: String,
    onEditClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit
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
                text = "$parkingName • $rating ★",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E3A8A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (comment.isBlank()) "No comment added." else comment,
                fontSize = 15.sp,
                color = Color(0xFF555555)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        onEditClick(parkingId)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF2563EB)
                    )
                ) {
                    Text("Edit")
                }

                OutlinedButton(
                    onClick = {
                        onDeleteClick(parkingId)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFD32F2F)
                    )
                ) {
                    Text("Delete")
                }
            }
        }
    }
}

