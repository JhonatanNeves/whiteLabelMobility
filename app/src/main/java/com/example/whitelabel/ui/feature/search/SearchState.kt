package com.example.whitelabel.ui.feature.search

import com.example.whitelabel.domain.model.PlaceSuggestion

data class SearchState(
    val originQuery: String = "Minha localização",
    val destinationQuery: String = "",
    val suggestions: List<PlaceSuggestion> = emptyList(),
    val isLoading: Boolean = false
)