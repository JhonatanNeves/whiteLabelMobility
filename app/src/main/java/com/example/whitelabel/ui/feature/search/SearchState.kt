package com.example.whitelabel.ui.feature.search

data class SearchState(
    val originQuery: String = "Minha localização",
    val destinationQuery: String = "",
    val suggestions: List<String> = emptyList(),
    val isLoading: Boolean = false
)