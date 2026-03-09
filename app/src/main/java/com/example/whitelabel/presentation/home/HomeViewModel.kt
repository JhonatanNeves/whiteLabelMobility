package com.example.whitelabel.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whitelabel.domain.location.LocationTracker
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (
    private val locationTracker: LocationTracker
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _effect = Channel<HomeEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnLocationPermissionGranted -> {
                fetchUserLocation()
            }
            is HomeEvent.OnLoadInitialData -> {
                loadInitialData()
            }
            is HomeEvent.OnLocationUpdated -> {
                _state.update { it.copy(userLocation = event.location) }
            }
            is HomeEvent.OnSearchDestinationClicked -> {
                viewModelScope.launch {
                    _effect.send(HomeEffect.NavigateToSearch)
                }
            }
        }
    }

    private fun fetchUserLocation() {
        viewModelScope.launch {
            val location = locationTracker.getCurrentLocation()
            if (location != null) {
                _state.update { it.copy(userLocation = location) }
            } else {
                _effect.send(HomeEffect.ShowToast("Não foi possível obter a localização. Verifique seu GPS."))
            }
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            delay(1000)

            val mockDrivers = listOf(
                LatLng(-21.1408, -44.2616), // Mock de coordenadas
                LatLng(-21.1450, -44.2600)
            )

            _state.update {
                it.copy(
                    isLoading = false,
                    nearbyDrivers = mockDrivers,
                    userLocation = LatLng(-21.14, -44.26)
                )
            }
        }
    }
}