package com.example.whitelabel.domain.model

import com.google.android.gms.maps.model.LatLng

data class RouteDetails(
    val points: List<LatLng>,
    val distance: String, // Ex: "6.4 km"
    val duration: String  // Ex: "14 min"
)