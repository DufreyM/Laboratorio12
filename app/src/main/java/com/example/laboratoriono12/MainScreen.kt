package com.example.laboratoriono12

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Bienvenido a SaludDirecta",
            fontSize = 22.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(
            onClick = { navController.navigate("capture_symptom") },
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(50.dp)
        ) {
            Text("Capturar Síntomas", fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var location by remember { mutableStateOf("Ubicación no disponible") }
    var symptomDescription by remember { mutableStateOf("") }

    NavHost(navController, startDestination = "main") {
        composable("main") { MainScreen(navController) }
        composable("capture_symptom") { CaptureSymptomScreen(navController) }
        composable("location_form") {
            LocationFormScreen(navController) { loc, symptoms ->
                location = loc
                symptomDescription = symptoms
                navController.navigate("confirmation")
            }
        }
        composable("confirmation") {
            ConfirmationScreen(navController, location, symptomDescription)
        }
    }
}

