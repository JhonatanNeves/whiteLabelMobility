package com.example.whitelabel.data.repository

import com.example.whitelabel.domain.model.LocationCoordinate
import com.example.whitelabel.domain.model.PlaceSuggestion
import com.example.whitelabel.domain.repository.SearchRepository
import com.example.whitelabel.BuildConfig
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val placesClient: PlacesClient
) : SearchRepository {

    override suspend fun getAutocomplete(query: String): Result<List<PlaceSuggestion>> {
        if (query.isBlank()) return Result.success(emptyList())

        return try {

            val request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .setCountries("BR")
                .build()

            val response = placesClient.findAutocompletePredictions(request).await()

            val suggestions = response.autocompletePredictions.map { prediction ->
                PlaceSuggestion(
                    placeId = prediction.placeId,
                    primaryText = prediction.getPrimaryText(null).toString(),
                    secondaryText = prediction.getSecondaryText(null).toString(),
                    description = ""
                )
            }

            Result.success(suggestions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchPlaces(query: String): Result<List<PlaceSuggestion>> = withContext(
        Dispatchers.IO) {
        try {
            // Documentação: https://developers.google.com/maps/documentation/places/web-service/autocomplete
            val url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?" +
                    "input=${query.replace(" ", "%20")}&" +
                    "types=geocode&" + // geocode foca em endereços e cidades
                    "key=${BuildConfig.MAPS_API_KEY}"

            val response = URL(url).readText()
            val json = JSONObject(response)

            val status = json.getString("status")
            if (status != "OK" && status != "ZERO_RESULTS") {
                return@withContext Result.failure(Exception("Erro Places API: $status"))
            }

            val predictions = json.getJSONArray("predictions")
            val suggestions = mutableListOf<PlaceSuggestion>()

            for (i in 0 until predictions.length()) {
                val item = predictions.getJSONObject(i)
                suggestions.add(
                    PlaceSuggestion(
                        description = item.getString("description"),
                        placeId = item.getString("place_id"),
                        primaryText = "Teste",
                        secondaryText = "test"
                    )
                )
            }

            Result.success(suggestions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPlaceCoordinates(placeId: String): Result<LocationCoordinate> {
        return try {
            val placeFields: List<Place.Field> = listOf(
                Place.Field.LOCATION,
                Place.Field.FORMATTED_ADDRESS
            )

            val request = FetchPlaceRequest.builder(placeId, placeFields).build()

            val response = placesClient.fetchPlace(request).await()
            val latLng = response.place.location

            if (latLng != null) {
                Result.success(
                    LocationCoordinate(
                        latitude = latLng.latitude,
                        longitude = latLng.longitude,
                        address = response.place.formattedAddress ?: ""
                    )
                )
            } else {
                Result.failure(Exception("Coordenadas não encontradas"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}