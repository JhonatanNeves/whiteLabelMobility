package com.example.whitelabel.data.repository

import com.example.whitelabel.BuildConfig
import com.example.whitelabel.domain.repository.DirectionsRepository
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import javax.inject.Inject

class DirectionsRepositoryImpl @Inject constructor() : DirectionsRepository {

    override suspend fun getRoute(origin: LatLng, destination: LatLng): Result<List<LatLng>> = withContext(Dispatchers.IO) {
        try {
            val originStr = "${origin.latitude},${origin.longitude}"
            val destStr = "${destination.latitude},${destination.longitude}"
            val url = "https://maps.googleapis.com/maps/api/directions/json?origin=$originStr&destination=$destStr&key=${BuildConfig.MAPS_API_KEY}"

            val response = URL(url).readText()
            val json = JSONObject(response)
            val routes = json.getJSONArray("routes")

            if (routes.length() > 0) {
                val points = routes.getJSONObject(0)
                    .getJSONObject("overview_polyline")
                    .getString("points")

                Result.success(PolyUtil.decode(points))
            } else {
                Result.failure(Exception("Nenhuma rota encontrada"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}