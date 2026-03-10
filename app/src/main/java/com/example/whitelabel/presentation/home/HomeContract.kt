package com.example.whitelabel.presentation.home


import com.google.android.gms.maps.model.LatLng
data class HomeState(
    val isLoading: Boolean = false,
    val userLocation: LatLng? = null,
    val nearbyDrivers: List<LatLng> = emptyList(),
    val userName: String = "Jhonatan"
)

sealed class HomeEvent {
    data object OnLoadInitialData : HomeEvent()
    data class OnLocationUpdated(val location: LatLng) : HomeEvent()
    data object OnSearchDestinationClicked : HomeEvent()

    data object OnLocationPermissionGranted : HomeEvent()

    data object OnScheduleClick : HomeEvent()
}

sealed class HomeEffect {
    data object NavigateToSearch : HomeEffect()
    data class ShowToast(val message: String) : HomeEffect()
}