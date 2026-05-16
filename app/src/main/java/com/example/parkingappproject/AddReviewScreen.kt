package com.example.parkingappproject

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parkingappproject.data.LocalReviewParking
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun AddReviewScreen(
    parkingId: String,
    onBackClick: () -> Unit,
    onSubmitClick: (LocalReviewParking) -> Unit
) {
    var cleanlinessRating by rememberSaveable { mutableStateOf(4) }
    var securityRating by rememberSaveable { mutableStateOf(5) }
    var priceRating by rememberSaveable { mutableStateOf(4) }
    var easyToFindRating by rememberSaveable { mutableStateOf(4) }
    var comment by rememberSaveable { mutableStateOf("") }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val calculatedRating =
        (cleanlinessRating + securityRating + priceRating + easyToFindRating) / 4.0

    fun submitReview() {
        val review = LocalReviewParking(
            parkingId = parkingId,
            cleanliness = cleanlinessRating,
            security = securityRating,
            priceRating = priceRating,
            easyToFind = easyToFindRating,
            calculatedRating = calculatedRating,
            comment = comment,
            createdAt = System.currentTimeMillis()
        )

        onSubmitClick(review)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFFF8E7)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = if (isLandscape) 28.dp else 24.dp,
                    vertical = if (isLandscape) 14.dp else 24.dp
                )
        ) {
            AddReviewTopBar(
                isLandscape = isLandscape,
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(if (isLandscape) 12.dp else 20.dp))

            if (isLandscape) {
                AddReviewLandscapeContent(
                    cleanlinessRating = cleanlinessRating,
                    onCleanlinessChange = { cleanlinessRating = it },
                    securityRating = securityRating,
                    onSecurityChange = { securityRating = it },
                    priceRating = priceRating,
                    onPriceChange = { priceRating = it },
                    easyToFindRating = easyToFindRating,
                    onEasyToFindChange = { easyToFindRating = it },
                    comment = comment,
                    onCommentChange = { comment = it },
                    calculatedRating = calculatedRating,
                    onSubmitClick = { submitReview() },
                    modifier = Modifier.weight(1f)
                )
            } else {
                AddReviewPortraitContent(
                    cleanlinessRating = cleanlinessRating,
                    onCleanlinessChange = { cleanlinessRating = it },
                    securityRating = securityRating,
                    onSecurityChange = { securityRating = it },
                    priceRating = priceRating,
                    onPriceChange = { priceRating = it },
                    easyToFindRating = easyToFindRating,
                    onEasyToFindChange = { easyToFindRating = it },
                    comment = comment,
                    onCommentChange = { comment = it },
                    calculatedRating = calculatedRating,
                    onSubmitClick = { submitReview() },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun AddReviewTopBar(
    isLandscape: Boolean,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.back),
            contentDescription = "Back",
            modifier = Modifier
                .size(if (isLandscape) 28.dp else 32.dp)
                .align(Alignment.CenterStart)
                .clickable {
                    onBackClick()
                }
        )

        Text(
            text = "Leave a Review",
            fontSize = if (isLandscape) 26.sp else 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E3A8A),
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun AddReviewPortraitContent(
    cleanlinessRating: Int,
    onCleanlinessChange: (Int) -> Unit,
    securityRating: Int,
    onSecurityChange: (Int) -> Unit,
    priceRating: Int,
    onPriceChange: (Int) -> Unit,
    easyToFindRating: Int,
    onEasyToFindChange: (Int) -> Unit,
    comment: String,
    onCommentChange: (String) -> Unit,
    calculatedRating: Double,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ReviewParkingSummaryCard()
        }

        item {
            RatingInputCard(
                title = "Rate your experience",
                cleanlinessRating = cleanlinessRating,
                onCleanlinessChange = onCleanlinessChange,
                securityRating = securityRating,
                onSecurityChange = onSecurityChange,
                priceRating = priceRating,
                onPriceChange = onPriceChange,
                easyToFindRating = easyToFindRating,
                onEasyToFindChange = onEasyToFindChange
            )
        }

        item {
            ReviewCommentField(
                comment = comment,
                onCommentChange = onCommentChange,
                height = 130.dp
            )
        }

        item {
            CalculatedRatingCard(calculatedRating = calculatedRating)
        }

        item {
            SubmitReviewButton(onSubmitClick = onSubmitClick)
        }
    }
}

@Composable
fun AddReviewLandscapeContent(
    cleanlinessRating: Int,
    onCleanlinessChange: (Int) -> Unit,
    securityRating: Int,
    onSecurityChange: (Int) -> Unit,
    priceRating: Int,
    onPriceChange: (Int) -> Unit,
    easyToFindRating: Int,
    onEasyToFindChange: (Int) -> Unit,
    comment: String,
    onCommentChange: (String) -> Unit,
    calculatedRating: Double,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = 18.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.72f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                ReviewParkingSummaryCard()

                RatingInputCard(
                    title = "Rate your experience",
                    cleanlinessRating = cleanlinessRating,
                    onCleanlinessChange = onCleanlinessChange,
                    securityRating = securityRating,
                    onSecurityChange = onSecurityChange,
                    priceRating = priceRating,
                    onPriceChange = onPriceChange,
                    easyToFindRating = easyToFindRating,
                    onEasyToFindChange = onEasyToFindChange
                )

                ReviewCommentField(
                    comment = comment,
                    onCommentChange = onCommentChange,
                    height = 115.dp
                )

                CalculatedRatingCard(
                    calculatedRating = calculatedRating
                )

                SubmitReviewButton(
                    onSubmitClick = onSubmitClick
                )
            }
        }
    }
}

@Composable
fun ReviewParkingSummaryCard() {
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
                text = "City Parking Stavroupoli",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E3A8A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Stavroupoli • 2.00€/hour",
                fontSize = 15.sp,
                color = Color(0xFF555555)
            )
        }
    }
}

@Composable
fun RatingInputCard(
    title: String,
    cleanlinessRating: Int,
    onCleanlinessChange: (Int) -> Unit,
    securityRating: Int,
    onSecurityChange: (Int) -> Unit,
    priceRating: Int,
    onPriceChange: (Int) -> Unit,
    easyToFindRating: Int,
    onEasyToFindChange: (Int) -> Unit
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
                text = title,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E3A8A)
            )

            Spacer(modifier = Modifier.height(16.dp))

            RatingSelectorRow(
                label = "Cleanliness",
                selectedRating = cleanlinessRating,
                onRatingSelected = onCleanlinessChange
            )

            Spacer(modifier = Modifier.height(12.dp))

            RatingSelectorRow(
                label = "Security",
                selectedRating = securityRating,
                onRatingSelected = onSecurityChange
            )

            Spacer(modifier = Modifier.height(12.dp))

            RatingSelectorRow(
                label = "Price satisfaction",
                selectedRating = priceRating,
                onRatingSelected = onPriceChange
            )

            Spacer(modifier = Modifier.height(12.dp))

            RatingSelectorRow(
                label = "Easy to find",
                selectedRating = easyToFindRating,
                onRatingSelected = onEasyToFindChange
            )
        }
    }
}

@Composable
fun RatingSelectorRow(
    label: String,
    selectedRating: Int,
    onRatingSelected: (Int) -> Unit
) {
    Column {
        Text(
            text = label,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF555555)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (number in 1..5) {
                RatingNumberButton(
                    number = number,
                    selected = number == selectedRating,
                    onClick = { onRatingSelected(number) }
                )
            }
        }
    }
}

@Composable
fun RatingNumberButton(
    number: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(40.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = if (selected) Color(0xFF2563EB) else Color(0xFFFFF8E7),
        shadowElevation = if (selected) 4.dp else 1.dp
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = number.toString(),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = if (selected) Color.White else Color(0xFF1E3A8A)
            )
        }
    }
}

@Composable
fun ReviewCommentField(
    comment: String,
    onCommentChange: (String) -> Unit,
    height: androidx.compose.ui.unit.Dp
) {
    OutlinedTextField(
        value = comment,
        onValueChange = { onCommentChange(it) },
        label = { Text("Write your experience") },
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
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
}

@Composable
fun CalculatedRatingCard(
    calculatedRating: Double
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Calculated rating",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E3A8A)
            )

            Text(
                text = "%.1f ★".format(calculatedRating),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF444444)
            )
        }
    }
}

@Composable
fun SubmitReviewButton(
    onSubmitClick: () -> Unit
) {
    Button(
        onClick = onSubmitClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF2563EB),
            contentColor = Color.White
        )
    ) {
        Text(
            text = "Submit Review",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

