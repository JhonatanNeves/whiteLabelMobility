package com.example.whitelabel.data.repository

import com.example.whitelabel.domain.model.LocationCoordinate
import com.example.whitelabel.domain.model.PlaceSuggestion
import com.example.whitelabel.domain.repository.SearchRepository
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.tasks.await
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
                    secondaryText = prediction.getSecondaryText(null).toString()
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