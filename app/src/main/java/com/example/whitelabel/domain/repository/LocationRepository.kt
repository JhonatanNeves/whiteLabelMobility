package com.example.whitelabel.domain.repository

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getLiveLocation(): Flow<LatLng>
}