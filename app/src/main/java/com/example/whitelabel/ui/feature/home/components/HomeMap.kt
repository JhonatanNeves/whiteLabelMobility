package com.example.whitelabel.ui.feature.home.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.whitelabel.ui.feature.home.HomeState
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline

@Composable
fun HomeMap(
    state: HomeState,
    cameraPositionState: CameraPositionState
) {
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = state.userLocation != null),
        uiSettings = MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false),
        contentPadding = PaddingValues(bottom = 140.dp, top = 100.dp)
    ) {
        // Linha da Rota
        if (state.routePolylines.isNotEmpty()) {
            Polyline(points = state.routePolylines, color = Color.DarkGray, width = 10f)
        }

        // Marcadores (Delegando para o componente de desenho)
        state.userLocation?.let { origin ->
            if (state.destinationLat != null) {
                OriginMarker(position = origin)
            }
        }

        if (state.destinationLat != null && state.destinationLng != null) {
            DestinationMarker(
                position = LatLng(state.destinationLat, state.destinationLng),
                address = state.destinationAddress ?: "",
                routeInfo = "6.4 KM • 14 MIN" // Depois vindo do State
            )
        }
    }
}