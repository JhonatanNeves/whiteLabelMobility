package com.example.whitelabel.ui.feature.search

import com.example.whitelabel.domain.model.PlaceSuggestion

sealed interface SearchEvent {
    data class OnOriginChanged(val query: String) : SearchEvent
    data class OnDestinationChanged(val query: String) : SearchEvent
    data class OnSuggestionSelected(val suggestion: PlaceSuggestion) : SearchEvent
    data object OnBackClick : SearchEvent
}