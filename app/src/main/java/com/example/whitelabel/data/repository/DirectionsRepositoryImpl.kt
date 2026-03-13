package com.example.whitelabel.data.repository

import com.example.whitelabel.BuildConfig
import com.example.whitelabel.domain.model.RouteDetails // 🔥 Importe o modelo novo
import com.example.whitelabel.domain.repository.DirectionsRepository
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import javax.inject.Inject

class DirectionsRepositoryImpl @Inject constructor() : DirectionsRepository {

    override suspend fun getRoute(origin: LatLng, destination: LatLng): Result<RouteDetails> = withContext(Dispatchers.IO) {
        try {
            val url = "https://maps.googleapis.com/maps/api/directions/json?" +
                    "origin=${origin.latitude},${origin.longitude}&" +
                    "destination=${destination.latitude},${destination.longitude}&" +
                    "mode=driving&" + // 🔥 Garante que a rota é para carros
                    "key=${BuildConfig.MAPS_API_KEY}"

            val response = URL(url).readText()
            val json = JSONObject(response)

            // 1. TRATAMENTO DE ERRO SÊNIOR: Verifique o Status
            val status = json.getString("status")
            if (status != "OK") {
                println("DEBUG: Erro na API do Google: $status")
                return@withContext Result.failure(Exception("Google API: $status"))
            }

            val routes = json.getJSONArray("routes")
            if (routes.length() > 0) {
                val route = routes.getJSONObject(0)
                val leg = route.getJSONArray("legs").getJSONObject(0)

                val distance = leg.getJSONObject("distance").getString("text")
                val duration = leg.getJSONObject("duration").getString("text")

                // 2. MELHORIA DE PRECISÃO:
                // Se a overview_polyline estiver muito "torta",
                // o ideal é extrair os pontos de cada 'step' dentro da 'leg'.
                // Por enquanto, vamos manter a overview, mas validar o decode.
                val pointsEnc = route.getJSONObject("overview_polyline").getString("points")
                val decodedPath = PolyUtil.decode(pointsEnc)

                println("DEBUG: Rota carregada com ${decodedPath.size} pontos.")

                Result.success(
                    RouteDetails(
                        points = decodedPath,
                        distance = distance,
                        duration = duration
                    )
                )
            } else {
                Result.failure(Exception("Caminho não encontrado"))
            }
        } catch (e: Exception) {
            println("DEBUG: Falha catastrófica: ${e.message}")
            Result.failure(e)
        }
    }
}