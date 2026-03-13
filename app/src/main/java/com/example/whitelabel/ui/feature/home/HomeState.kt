package com.example.whitelabel.ui.feature.home

import com.google.android.gms.maps.model.LatLng

data class HomeState(
    val userName: String = "",
    val userLocation: LatLng? = null,
    val nearbyDrivers: List<LatLng> = emptyList(),
    val isLoading: Boolean = false,
    val destinationAddress: String? = null,
    val destinationLat: Double? = null,
    val destinationLng: Double? = null,
    val distance: String? = null, //  Novo
    val duration: String? = null,  //  Novo
    val routePolylines: List<LatLng> = emptyList()
)