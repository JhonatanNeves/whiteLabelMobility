package com.example.whitelabel.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whitelabel.data.repository.LocationRepository
import com.example.whitelabel.domain.repository.DirectionsRepository // 🔥 Import do novo repositório
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val directionsRepository: DirectionsRepository // Injetando a lógica de rotas
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
                // 1 Atualizamos os dados básicos do destino no Estado
                _state.update { it.copy(
                    destinationAddress = event.address,
                    destinationLat = event.latitude,
                    destinationLng = event.longitude
                ) }

                // 2 Disparamos a busca da rota (Linha azul)
                val currentOrigin = _state.value.userLocation
                if (currentOrigin != null) {
                    calculateRoute(
                        origin = currentOrigin,
                        destination = LatLng(event.latitude, event.longitude)
                    )
                }
            }
        }
    }

    // Nova função privada para gerenciar a busca da rota
    private fun calculateRoute(origin: LatLng, destination: LatLng) {
        viewModelScope.launch {
            directionsRepository.getRoute(origin, destination)
                .onSuccess { polylinePoints ->
                    // Se o Google devolveu os pontos, atualizamos o estado
                    _state.update { it.copy(routePolylines = polylinePoints) }
                }
                .onFailure { error ->
                    // Aqui você poderia atualizar o estado com uma mensagem de erro
                    println("Erro ao buscar rota: ${error.message}")
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