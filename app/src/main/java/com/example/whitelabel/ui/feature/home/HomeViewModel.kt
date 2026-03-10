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

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnLoadInitialData -> fetchLocation()
            is HomeEvent.OnLocationPermissionGranted -> fetchLocation()
            is HomeEvent.OnScheduleClick -> { /* Lógica de agendamento */ }
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