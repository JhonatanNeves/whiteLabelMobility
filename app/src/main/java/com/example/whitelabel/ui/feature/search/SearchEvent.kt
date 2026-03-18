package com.example.whitelabel.ui.feature.search

import com.example.whitelabel.domain.model.PlaceSuggestion
import com.google.android.gms.maps.model.LatLng

sealed interface SearchEvent {
    data class OnOriginChanged(val query: String) : SearchEvent
    data class OnDestinationChanged(val query: String) : SearchEvent
    data class OnSuggestionSelected(val suggestion: PlaceSuggestion) : SearchEvent
    data object OnBackClick : SearchEvent

    data class OnInitialize(val userLatLng: LatLng) : SearchEvent

    //  Evento de inicialização
    data class OnQueryChange(val query: String) : SearchEvent
}