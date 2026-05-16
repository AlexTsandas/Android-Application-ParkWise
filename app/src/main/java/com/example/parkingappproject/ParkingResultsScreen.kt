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
import androidx.compose.runtime.saveable.rememberSaveable
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

@Composable
fun ParkingResultsScreen(
    parkings: List<Parking>,
    savedParkingIds: Set<String>,
    parkingRatings: Map<String, Double>,
    searchText: String,
    onSearchClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onParkingClick: (String) -> Unit,
    onLeaveReviewClick: (String) -> Unit,
    onSaveClick: (Parking, String) -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    var resultsSearchText by rememberSaveable(searchText) {
        mutableStateOf(searchText)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFFF8E7)
    ) {
        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.85f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Top
                ) {
                    Spacer(modifier = Modifier.height(10.dp))

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
                        text = "Parking Results",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E3A8A)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    SearchArea(
                        resultsSearchText = resultsSearchText,
                        onSearchTextChange = { resultsSearchText = it },
                        onSearchClick = onSearchClick
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .weight(1.25f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    if (parkings.isEmpty()) {
                        item {
                            EmptyResultsCard()
                        }
                    } else {
                        items(
                            items = parkings,
                            key = { it.parkingId }
                        ) { parking ->
                            ParkingCard(
                                parkingId = parking.parkingId,
                                name = parking.name,
                                area = parking.area,
                                price = "${parking.pricePerHour}€/hour",
                                rating = formatParkingRating(
                                    parkingRatings[parking.parkingId] ?: 0.0
                                ),
                                isSaved = savedParkingIds.contains(parking.parkingId),
                                onCardClick = {
                                    onParkingClick(parking.parkingId)
                                },
                                onSaveClick = { note ->
                                    onSaveClick(parking, note)
                                },
                                onLeaveReviewClick = {
                                    onLeaveReviewClick(parking.parkingId)
                                }
                            )
                        }
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
                    text = "Parking Results",
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
                        SearchArea(
                            resultsSearchText = resultsSearchText,
                            onSearchTextChange = { resultsSearchText = it },
                            onSearchClick = onSearchClick
                        )
                    }

                    if (parkings.isEmpty()) {
                        item {
                            EmptyResultsCard()
                        }
                    } else {
                        items(
                            items = parkings,
                            key = { it.parkingId }
                        ) { parking ->
                            ParkingCard(
                                parkingId = parking.parkingId,
                                name = parking.name,
                                area = parking.area,
                                price = "${parking.pricePerHour}€/hour",
                                rating = formatParkingRating(
                                    parkingRatings[parking.parkingId] ?: 0.0
                                ),
                                isSaved = savedParkingIds.contains(parking.parkingId),
                                onCardClick = {
                                    onParkingClick(parking.parkingId)
                                },
                                onSaveClick = { note ->
                                    onSaveClick(parking, note)
                                },
                                onLeaveReviewClick = {
                                    onLeaveReviewClick(parking.parkingId)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

fun formatParkingRating(rating: Double): String {
    return if (rating <= 0.0) {
        "No reviews yet"
    } else {
        "%.1f ★".format(rating)
    }
}

@Composable
fun SearchArea(
    resultsSearchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearchClick: (String) -> Unit
) {
    Column {
        OutlinedTextField(
            value = resultsSearchText,
            onValueChange = { onSearchTextChange(it) },
            label = { Text("City, Area") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
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

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                onSearchClick(resultsSearchText)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2563EB),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Search",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ParkingCard(
    parkingId: String,
    name: String,
    area: String,
    price: String,
    rating: String,
    isSaved: Boolean,
    onCardClick: () -> Unit,
    onSaveClick: (String) -> Unit,
    onLeaveReviewClick: () -> Unit
) {
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
                text = "Rating: $rating",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF444444)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (isSaved) {
                    Button(
                        onClick = {
                            onSaveClick("")
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
                } else {
                    OutlinedButton(
                        onClick = {
                            showNoteDialog = true
                        },
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
                    onClick = {
                        onLeaveReviewClick()
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
            }
        }
    }
}

@Composable
fun EmptyResultsCard() {
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
            text = "No parking areas found.",
            fontSize = 15.sp,
            color = Color(0xFF555555),
            modifier = Modifier.padding(18.dp)
        )
    }
}

