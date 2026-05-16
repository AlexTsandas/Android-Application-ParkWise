package com.example.parkingappproject

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(
    onLoginClick: (String, String) -> Unit,
    errorMessage: String
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFFF8E7)
    ) {
        if (isLandscape) {
            LoginLandscapeContent(
                email = email,
                onEmailChange = { email = it },
                password = password,
                onPasswordChange = { password = it },
                errorMessage = errorMessage,
                onLoginClick = {
                    onLoginClick(email, password)
                }
            )
        } else {
            LoginPortraitContent(
                email = email,
                onEmailChange = { email = it },
                password = password,
                onPasswordChange = { password = it },
                errorMessage = errorMessage,
                onLoginClick = {
                    onLoginClick(email, password)
                }
            )
        }
    }
}

@Composable
fun LoginPortraitContent(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    errorMessage: String,
    onLoginClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "ParkWise",
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E3A8A),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 60.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(22.dp))

            Image(
                painter = painterResource(id = R.drawable.parking),
                contentDescription = "Parking Image",
                modifier = Modifier.size(102.dp)
            )

            Spacer(modifier = Modifier.height(44.dp))

            LoginFormFields(
                email = email,
                onEmailChange = onEmailChange,
                password = password,
                onPasswordChange = onPasswordChange,
                errorMessage = errorMessage,
                onLoginClick = onLoginClick
            )
        }
    }
}

@Composable
fun LoginLandscapeContent(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    errorMessage: String,
    onLoginClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 34.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "ParkWise",
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E3A8A),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            Image(
                painter = painterResource(id = R.drawable.parking),
                contentDescription = "Parking Image",
                modifier = Modifier.size(110.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Find and save parking areas easily",
                fontSize = 16.sp,
                color = Color(0xFF555555),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.width(36.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoginFormFields(
                email = email,
                onEmailChange = onEmailChange,
                password = password,
                onPasswordChange = onPasswordChange,
                errorMessage = errorMessage,
                onLoginClick = onLoginClick
            )
        }
    }
}

@Composable
fun LoginFormFields(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    errorMessage: String,
    onLoginClick: () -> Unit
) {
    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        label = { Text("Email") },
        modifier = Modifier.fillMaxWidth(),
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

    Spacer(modifier = Modifier.height(18.dp))

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text("Password") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        visualTransformation = PasswordVisualTransformation(),
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

    Text(
        text = errorMessage,
        color = Color(0xFFD32F2F),
        fontSize = 14.sp
    )

    Spacer(modifier = Modifier.height(18.dp))

    Button(
        onClick = onLoginClick,
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
            text = "Login",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
