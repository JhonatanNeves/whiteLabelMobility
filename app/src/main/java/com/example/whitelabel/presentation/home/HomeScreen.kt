package com.example.whitelabel.presentation.home

import com.google.android.gms.maps.CameraUpdateFactory
import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whitelabel.core.theme.WhiteLabelTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.draw.clip

@Composable
fun HomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
    onNavigateToSearch: () -> Unit
) {
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineLocationGranted || coarseLocationGranted) {
            onEvent(HomeEvent.OnLocationPermissionGranted)
        } else {
            Toast.makeText(context, "Precisamos da localização para o app funcionar", Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
        onEvent(HomeEvent.OnLoadInitialData)
    }

    LaunchedEffect(Unit) {

    }

    LaunchedEffect(Unit) {
        onEvent(HomeEvent.OnLoadInitialData)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(
                state.userLocation ?: LatLng(0.0, 0.0),
                15f
            )
        }

        LaunchedEffect(state.userLocation) {
            state.userLocation?.let { loc ->
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newLatLngZoom(loc, 15f),
                    durationMs = 1500
                )
            }
        }

        val mapProperties = MapProperties(
            isMyLocationEnabled = state.userLocation != null
        )

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = MapUiSettings(zoomControlsEnabled = false)
        ) {
            state.nearbyDrivers.forEach { driverLoc ->
                Marker(
                    state = MarkerState(position = driverLoc),
                    title = "Motorista Próximo"
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .padding(14.dp)
        ) {
            Text(
                text = "Boa tarde, ${state.userName}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(3.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    ,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Barra de Busca "Para onde?"
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                        .clip(RoundedCornerShape(27.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .clickable { onNavigateToSearch() }
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Para onde?",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .size(54.dp) // Mesma altura da barra de busca
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        // Borda que simula o cilindro vazado (vista de cima)
                        .border(2.dp, Color.LightGray.copy(alpha = 0.5f), CircleShape)
                        .clickable { onEvent(HomeEvent.OnScheduleClick) },
                    contentAlignment = Alignment.Center
                ) {
                    // Ícone Central: Calendário
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Agendar",
                        modifier = Modifier.size(26.dp),
                        tint = Color.Black
                    )

                    // Ícone de Relógio (Atividade Recente) no canto inferior direito
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = (-2).dp, y = (-2).dp) // Ajuste fino de posição
                            .size(18.dp)
                            .background(Color.White, CircleShape) // Fundo para não misturar com o calendário
                            .padding(1.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = "Recentes",
                            tint = Color.Black,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }

    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    WhiteLabelTheme {
        HomeScreen(
            state = HomeState(
                userName = "Usuário Teste",
                userLocation = LatLng(-23.5505, -46.6333), // Example location: São Paulo
                nearbyDrivers = listOf(
                    LatLng(-23.5510, -46.6340),
                    LatLng(-23.5490, -46.6320)
                )
            ),
            onEvent = {}, // Dummy lambda for preview
            onNavigateToSearch = {} // Dummy lambda for preview
        )
    }
}


