package com.example.whitelabel.data.repository

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getLiveLocation(): Flow<LatLng>
}