package com.example.whitelabel.ui.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.whitelabel.core.theme.WhiteLabelTheme
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import androidx.compose.runtime.remember

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
    onNavigateToSearch: () -> Unit
) {
    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(state.userLocation) {
        state.userLocation?.let { location ->
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(location, 15f),
                durationMs = 1500
            )
        }
    }

    LaunchedEffect(state.destinationLat, state.destinationLng) {
        val lat = state.destinationLat
        val lng = state.destinationLng
        if (lat != null && lng != null) {
            val destination = LatLng(lat, lng)
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(destination, 16f), // Zoom um pouco mais perto
                durationMs = 1500
            )
        }
    }

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded
        )
    )

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 120.dp,
        sheetShape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp),
        sheetContainerColor = Color.White,
        sheetDragHandle = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(32.dp)
                        .height(4.dp)
                        .background(Color.LightGray, CircleShape)
                )
            }
        },
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Good evening, ${state.userName}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.W500),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp)
                            .clip(RoundedCornerShape(27.dp))
                            .background(Color(0xFFF3F3F3))
                            .clickable { onNavigateToSearch() }
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Color.Black)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = state.destinationAddress ?: "Choose a Destination",
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (state.destinationAddress != null) Color.Black else Color.Gray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, Color.LightGray.copy(alpha = 0.5f), CircleShape)
                            .clickable { onEvent(HomeEvent.OnScheduleClick) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(24.dp))
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .offset(x = (-4).dp, y = (-4).dp)
                                .size(16.dp)
                                .background(Color.White, CircleShape)
                                .padding(1.dp)
                        ) {
                            Icon(Icons.Default.Schedule, contentDescription = null, tint = Color.Black)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(9.dp))
                HorizontalDivider(color = Color(0xFFF3F3F3))
            }
        }
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = state.userLocation != null),
                uiSettings = MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false),
                contentPadding = PaddingValues(bottom = 120.dp)
            ) {

                val destLat = state.destinationLat
                val destLng = state.destinationLng

                if (destLat != null && destLng != null) {

                    val markerState = remember(destLat, destLng) {
                        MarkerState(position = LatLng(destLat, destLng))
                    }

                    Marker(
                        state = markerState,
                        title = state.destinationAddress,
                        snippet = "Destino Selecionado"
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(26.dp),
                    color = Color(0xFFF3C111),
                    shadowElevation = 6.dp
                ) {
                    Text(
                        text = "Moto SJ",
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
                        fontWeight = FontWeight.W500,
                        color = Color.Black
                    )
                }

                Box {
                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 6.dp,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            modifier = Modifier.padding(12.dp),
                            tint = Color.Black
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(Color.Red, CircleShape)
                            .align(Alignment.TopEnd)
                            .border(2.dp, Color.White, CircleShape)
                    )
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
                userLocation = LatLng(-23.5505, -46.6333),
                nearbyDrivers = listOf(
                    LatLng(-23.5510, -46.6340),
                    LatLng(-23.5490, -46.6320)
                )
            ),
            onEvent = {},
            onNavigateToSearch = {}
        )
    }
}