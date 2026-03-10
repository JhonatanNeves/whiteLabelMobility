package com.example.whitelabel.ui.feature.home

import com.google.android.gms.maps.model.LatLng

data class HomeState(
    val userName: String = "",
    val userLocation: LatLng? = null,
    val nearbyDrivers: List<LatLng> = emptyList(),
    val isLoading: Boolean = false
)