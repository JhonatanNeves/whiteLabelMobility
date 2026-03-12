package com.example.whitelabel.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whitelabel.data.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        _state.update { it.copy(userName = "Jhonatan") }

        fetchLocation()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnLoadInitialData -> fetchLocation()
            is HomeEvent.OnLocationPermissionGranted -> fetchLocation()
            is HomeEvent.OnScheduleClick -> { /* Lógica de agendamento */ }

            is HomeEvent.OnDestinationSelected -> {
                // 1. Atualizamos a tela (A barra de busca vai mudar de texto)
                _state.update { it.copy(
                    destinationAddress = event.address,
                    destinationLat = event.latitude,
                    destinationLng = event.longitude
                ) }

                // 2. Aqui você poderá disparar um HomeEffect.MoveCameraTo(lat, lng) no futuro!
            }
        }
    }

    private fun fetchLocation() {
        viewModelScope.launch {
            locationRepository.getLiveLocation()
                .onStart { _state.update { it.copy(isLoading = true) } }
                .collect { location ->
                    _state.update { it.copy(userLocation = location, isLoading = false) }
                }
        }
    }
}