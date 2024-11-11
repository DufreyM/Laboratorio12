package com.example.laboratoriono12

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*

@Composable
fun LocationFormScreen(navController: NavController, onConfirm: (String, String) -> Unit) {
    var symptomDescription by remember { mutableStateOf("") }
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    var location by remember { mutableStateOf("Ubicación no disponible") }
    var hasLocationPermission by remember { mutableStateOf(false) }

    // Launcher for requesting location permission
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasLocationPermission = isGranted
            if (isGranted) {
                fetchLocationWithAddress(context, fusedLocationClient) { loc ->
                    location = loc ?: "Ubicación no disponible"
                }
            } else {
                location = "Permiso de ubicación denegado"
            }
        }
    )

    // Check and request location permission when the screen is first launched
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            hasLocationPermission = true
            fetchLocationWithAddress(context, fusedLocationClient) { loc ->
                location = loc ?: "Ubicación no disponible"
            }
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Formulario de Ubicación",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Text(
            "Ubicación Actual:",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            location,
            fontSize = 16.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))
        TextField(
            value = symptomDescription,
            onValueChange = { symptomDescription = it },
            label = { Text("Describe los síntomas") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                onConfirm(location, symptomDescription)
            },
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(50.dp)
        ) {
            Text("Enviar")
        }
    }
}

// Function to fetch location with address information
fun fetchLocationWithAddress(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    onLocationFetched: (String?) -> Unit
) {
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        val geocoder = Geocoder(context, Locale.getDefault())
        fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
            if (loc != null) {
                val latitude = loc.latitude
                val longitude = loc.longitude
                val addressList = geocoder.getFromLocation(latitude, longitude, 1)
                val address = if (addressList != null && addressList.isNotEmpty()) {
                    "${addressList[0].getAddressLine(0)}\nCoordenadas: $latitude, $longitude"
                } else {
                    "Ubicación desconocida\nCoordenadas: $latitude, $longitude"
                }
                onLocationFetched(address)
            } else {
                onLocationFetched("Ubicación no disponible")
            }
        }.addOnFailureListener {
            onLocationFetched("Ubicación no disponible")
        }
    } else {
        onLocationFetched("Permiso de ubicación no otorgado")
    }
}
