package com.example.whitelabel.ui.feature.main

sealed interface MainEvent {
    object OnLoadInitialData : MainEvent
    data class OnTabSelected(val index: Int) : MainEvent
    data class OnDestinationSelected(
        val latitude: Double,
        val longitude: Double,
        val address: String
    ) : MainEvent
}