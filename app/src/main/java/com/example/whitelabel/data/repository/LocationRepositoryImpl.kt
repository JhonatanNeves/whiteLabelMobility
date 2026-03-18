package com.example.whitelabel.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.os.Looper
import com.example.whitelabel.domain.repository.LocationRepository
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient,
    @ApplicationContext private val context: Context
) : LocationRepository {

    override suspend fun getAddressFromLatLng(latLng: LatLng): String? = withContext(Dispatchers.IO) {
        return@withContext try {

            val geocoder = Geocoder(context, Locale.getDefault())

            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                // "Thoroughfare" é a rua, "FeatureName" costuma ser o número
                val street = address.thoroughfare ?: ""
                val number = address.featureName ?: ""

                if (street.isNotEmpty()) "$street, $number" else "Endereço não identificado"
            } else {
                "Localização desconhecida"
            }
        } catch (e: Exception) {
            null
        }
    }

    @SuppressLint("MissingPermission")
    override suspend fun getLiveLocation(): Flow<LatLng> = callbackFlow {
        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let {
                    trySend(LatLng(it.latitude, it.longitude))
                }
            }
        }

        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000).build()
        fusedLocationClient.requestLocationUpdates(request, callback, Looper.getMainLooper())

        awaitClose { fusedLocationClient.removeLocationUpdates(callback) }
    }
}