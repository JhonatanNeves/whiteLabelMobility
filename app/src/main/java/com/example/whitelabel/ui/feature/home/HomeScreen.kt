package com.example.whitelabel.ui.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
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
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

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
                update = CameraUpdateFactory.newLatLngZoom(
                    destination,
                    16f
                ), // Zoom um pouco mais perto
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp)
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(27.dp))
                            .background(Color(0xFFF3F3F3))
                            .clickable { onNavigateToSearch() }
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = Color.Black
                            )
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
                            .border(
                                1.dp,
                                Color.LightGray.copy(alpha = 0.5f),
                                CircleShape
                            )
                            .clickable { onEvent(HomeEvent.OnScheduleClick) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.CalendarMonth,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .offset(x = (-4).dp, y = (-4).dp)
                                .size(16.dp)
                                .background(Color.White, CircleShape)
                                .padding(1.dp)
                        ) {
                            Icon(
                                Icons.Default.Schedule,
                                contentDescription = null,
                                tint = Color.Black
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(9.dp))
                HorizontalDivider(color = Color(0xFFF3F3F3))
            }
        }
    ) { // This is the content slot for the BottomSheetScaffold
        // The rest of the UI that appears behind the bottom sheet goes here.

        // Use a Box as the main container for the map and other elements behind the sheet.
        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = state.userLocation != null),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    myLocationButtonEnabled = false
                ),
                // Adjust contentPadding to account for the bottom sheet's height
                contentPadding = PaddingValues(
                    bottom = 120.dp, // This should align with sheetPeekHeight
                    top = 100.dp // Adjust as needed for other top elements
                )
            ) {
                // Map-specific composables like markers and polylines go inside this lambda.

                // 1. DESENHA A LINHA DA ROTA (Cor Cinza como na foto)
                if (state.routePolylines.isNotEmpty()) {
                    Polyline(
                        points = state.routePolylines,
                        color = Color.DarkGray,
                        width = 10f
                    )
                }

                // 2. TAG DA ORIGEM (Sua localização)
                val originLat = state.userLocation?.latitude
                val originLng = state.userLocation?.longitude

                if (originLat != null && originLng != null && state.destinationLat != null) {
                    MarkerComposable(
                        state = rememberMarkerState(position = LatLng(originLat, originLng)),
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                                color = Color.White,
                                shadowElevation = 4.dp
                            ) {
                                Row(
                                    modifier = Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 8.dp
                                    ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Sua localização",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.DarkGray,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(
                                        Icons.Default.Edit, contentDescription = null,
                                        modifier = Modifier.size(16.dp), tint = Color.Gray
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        Color(0xFF4A4E54),
                                        CircleShape
                                    )
                                    .border(
                                        2.dp,
                                        Color.White, CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                // 3. TAG DO DESTINO (Com a caixinha preta de Tempo/Distância embaixo)
                val destLat = state.destinationLat
                val destLng = state.destinationLng

                if (destLat != null && destLng != null) {
                    MarkerComposable(
                        state = rememberMarkerState(position = LatLng(destLat, destLng)),
                    ) {
                        Row(verticalAlignment = Alignment.Top) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color(0xFFF3C111), CircleShape)
                                    .border(2.dp,
                                        Color.White, CircleShape)
                                    .offset(y = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Flag,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Surface(
                                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                                    color = Color.White,
                                    shadowElevation = 4.dp
                                ) {
                                    Row(
                                        modifier = Modifier.padding(
                                            horizontal = 12.dp,
                                            vertical = 8.dp
                                        ),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = state.destinationAddress ?: "Destino",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.Black,
                                            modifier = Modifier.widthIn(max = 150.dp),
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Icon(
                                            Icons.Default.Edit, contentDescription = null,
                                            modifier = Modifier.size(16.dp), tint = Color.Gray
                                        )
                                    }
                                }
                                Surface(
                                    shape = RoundedCornerShape(
                                        bottomStart = 8.dp,
                                        bottomEnd = 8.dp
                                    ),
                                    color = Color(0xFF2C323A),
                                    modifier = Modifier.padding(start = 12.dp)
                                ) {
                                    Text(
                                        text = "6.4 KM • 14 MIN",
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(
                                            horizontal = 10.dp,
                                            vertical = 4.dp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Place other UI elements that should appear OVER the map here.
            // For example, the top bar with "MoVee" and notifications.
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
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(26.dp),
                    color = Color(0xFFF3C111),
                    shadowElevation = 6.dp
                ) {
                    Text(
                        text = "MoVee",
                        modifier = Modifier.padding(
                            horizontal = 14.dp,
                            vertical = 14.dp
                        ),
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
            state = HomeState(),
            onEvent = {},
            onNavigateToSearch = {}
        )
    }
}
