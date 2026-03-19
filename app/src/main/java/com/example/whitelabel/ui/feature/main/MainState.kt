package com.example.whitelabel.ui.feature.main

import com.google.android.gms.maps.model.LatLng

data class MainState(
    val selectedTabIndex: Int = 0,
    val userLocation: LatLng? = null, // 🔥 Deve estar aqui!
    val destinationLat: Double? = null,
    val destinationLng: Double? = null,
    val destinationAddress: String? = null,
    val routePolylines: List<LatLng> = emptyList(),
    val distance: String? = null,
    val duration: String? = null,
    val isLoading: Boolean = false
)