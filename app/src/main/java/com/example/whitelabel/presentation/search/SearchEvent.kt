package com.example.whitelabel.presentation.search

sealed class SearchEvent {
    data class OnQueryChanged(val query: String) : SearchEvent()
    data object OnSearch : SearchEvent()
    data object OnBackPress : SearchEvent()
}
    