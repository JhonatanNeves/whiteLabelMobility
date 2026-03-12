package com.example.whitelabel.domain.repository

import com.example.whitelabel.domain.model.LocationCoordinate
import com.example.whitelabel.domain.model.PlaceSuggestion

interface SearchRepository {
    suspend fun getAutocomplete(query: String): Result<List<PlaceSuggestion>>

    suspend fun getPlaceCoordinates(placeId: String): Result<LocationCoordinate>
}