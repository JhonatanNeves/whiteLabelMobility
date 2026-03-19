package com.example.whitelabel.ui.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whitelabel.domain.repository.DirectionsRepository
import com.example.whitelabel.domain.repository.LocationRepository
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val directionsRepository: DirectionsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    init {
        // Começa a monitorar a localização do usuário assim que o app abre
        fetchLocation()
    }

    fun onEvent(event: MainEvent) {

        when (event) {
            is MainEvent.OnDestinationSelected -> {
                // 1. Atualiza o estado com o destino vindo da busca
                _state.update {
                    it.copy(
                        destinationLat = event.latitude,
                        destinationLng = event.longitude,
                        destinationAddress = event.address
                    )
                }

                // 2. Dispara a busca da rota (calculateRoute)
                val currentOrigin = _state.value.userLocation
                if (currentOrigin != null) {
                    calculateRoute(
                        origin = currentOrigin,
                        destination = LatLng(event.latitude, event.longitude)
                    )
                }
            }
            is MainEvent.OnLoadInitialData -> fetchLocation()
            else -> {}
        }
    }

    // 🔥 ESSA FUNÇÃO ESTAVA FALTANDO (Por isso o erro vermelho)
    private fun calculateRoute(origin: LatLng, destination: LatLng) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            directionsRepository.getRoute(origin, destination)
                .onSuccess { routeDetails ->
                    _state.update { it.copy(
                        routePolylines = routeDetails.points,
                        distance = routeDetails.distance,
                        duration = routeDetails.duration,
                        isLoading = false
                    ) }
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }


    private fun fetchLocation() {
        viewModelScope.launch {
            locationRepository.getLiveLocation()
                .collect { location ->
                    _state.update { it.copy(userLocation = location) }
                }
        }
    }
}