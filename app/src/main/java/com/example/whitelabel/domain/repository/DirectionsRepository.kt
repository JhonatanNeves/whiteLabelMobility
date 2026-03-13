package com.example.whitelabel.domain.repository

import com.example.whitelabel.domain.model.RouteDetails
import com.google.android.gms.maps.model.LatLng

interface DirectionsRepository {
    // Retorna uma lista de pontos (Polyline) ou um erro
    suspend fun getRoute(origin: LatLng, destination: LatLng): Result<RouteDetails>
}