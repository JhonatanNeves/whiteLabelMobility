package com.example.whitelabel.ui.feature.search

sealed interface SearchEvent {
    data class OnOriginChanged(val query: String) : SearchEvent
    data class OnDestinationChanged(val query: String) : SearchEvent
    data class OnSuggestionSelected(val address: String) : SearchEvent
    data object OnBackClick : SearchEvent
}