package com.example.parkingappproject

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    onFindParkingClick: (String) -> Unit,
    onHistoryClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    var searchText by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFFF8E7)
    ) {
        if (isLandscape) {
            HomeLandscapeContent(
                searchText = searchText,
                onSearchTextChange = { searchText = it },
                onFindParkingClick = onFindParkingClick,
                onHistoryClick = onHistoryClick,
                onLogoutClick = onLogoutClick
            )
        } else {
            HomePortraitContent(
                searchText = searchText,
                onSearchTextChange = { searchText = it },
                onFindParkingClick = onFindParkingClick,
                onHistoryClick = onHistoryClick,
                onLogoutClick = onLogoutClick
            )
        }
    }
}

@Composable
fun HomePortraitContent(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onFindParkingClick: (String) -> Unit,
    onHistoryClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(28.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocalParking,
                contentDescription = "ParkWise Icon",
                tint = Color(0xFF2563EB),
                modifier = Modifier.size(42.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "ParkWise",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E3A8A)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Find, save and review parking areas near your destination.",
            fontSize = 15.sp,
            color = Color(0xFF555555)
        )

        Spacer(modifier = Modifier.height(32.dp))

        SearchSectionCard(
            searchText = searchText,
            onSearchTextChange = onSearchTextChange,
            onFindParkingClick = onFindParkingClick
        )

        Spacer(modifier = Modifier.height(18.dp))

        HistorySectionCard(
            onHistoryClick = onHistoryClick
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onLogoutClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD32F2F),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Logout",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun HomeLandscapeContent(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onFindParkingClick: (String) -> Unit,
    onHistoryClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(0.9f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                imageVector = Icons.Default.LocalParking,
                contentDescription = "ParkWise Icon",
                tint = Color(0xFF2563EB),
                modifier = Modifier.size(58.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "ParkWise",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E3A8A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Search parking areas, save useful spots and review your experience.",
                fontSize = 15.sp,
                color = Color(0xFF555555),
                lineHeight = 20.sp
            )
        }

        Column(
            modifier = Modifier
                .weight(1.2f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            SearchSectionCard(
                searchText = searchText,
                onSearchTextChange = onSearchTextChange,
                onFindParkingClick = onFindParkingClick
            )

            Spacer(modifier = Modifier.height(14.dp))

            HistorySectionCard(
                onHistoryClick = onHistoryClick
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD32F2F),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Logout",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun SearchSectionCard(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onFindParkingClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = Color(0xFF2563EB),
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Where are you going?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E3A8A)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = searchText,
                onValueChange = { onSearchTextChange(it) },
                label = { Text("City, Area") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
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

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = {
                    onFindParkingClick(searchText)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2563EB),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Find Parking",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun HistorySectionCard(
    onHistoryClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onHistoryClick()
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = "History Icon",
                tint = Color(0xFF2563EB),
                modifier = Modifier.size(30.dp)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "My Parking History",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E3A8A)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "View your saved parkings and reviews",
                    fontSize = 14.sp,
                    color = Color(0xFF555555)
                )
            }
        }
    }
}

