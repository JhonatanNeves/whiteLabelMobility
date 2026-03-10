package com.example.whitelabel.ui.feature.home

sealed interface HomeEvent {
    data object OnLoadInitialData : HomeEvent
    data object OnLocationPermissionGranted : HomeEvent
    data object OnScheduleClick : HomeEvent
}