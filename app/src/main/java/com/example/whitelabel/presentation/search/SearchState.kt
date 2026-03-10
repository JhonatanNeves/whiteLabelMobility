package com.example.whitelabel.presentation.search

data class SearchState(
    val searchQuery: String = "",
    val searchResults: List<String> = emptyList(), // Example: list of addresses or places
    val isLoading: Boolean = false,
    val error: String? = null
)
