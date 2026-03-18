package com.example.whitelabel.ui.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whitelabel.domain.repository.LocationRepository
import com.example.whitelabel.domain.repository.SearchRepository
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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
class SearchViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val searchRepository: SearchRepository,
) : ViewModel() {

    private var searchJob: Job? = null

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private val _effect = Channel<SearchEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnInitialize -> {
                fetchCurrentAddress(event.userLatLng)
            }
            is SearchEvent.OnOriginChanged -> {
                _state.update { it.copy(originQuery = event.query) }
                updateSuggestions(event.query)
            }
            is SearchEvent.OnDestinationChanged -> {
                _state.update { it.copy(destinationQuery = event.query) }
                updateSuggestions(event.query) // 🔥 Usa a versão otimizada com debounce
            }
            is SearchEvent.OnQueryChange -> {
                _state.update { it.copy(destinationQuery = event.query) }
                updateSuggestions(event.query) // 🔥 Consolidado
            }
            is SearchEvent.OnSuggestionSelected -> {
                getCoordinates(event.suggestion.placeId)
            }
            is SearchEvent.OnBackClick -> {
                viewModelScope.launch { _effect.send(SearchEffect.NavigateBack) }
            }
        }
    }

    private fun fetchCurrentAddress(latLng: LatLng) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val address = locationRepository.getAddressFromLatLng(latLng)

            _state.update {
                it.copy(
                    originAddress = address ?: "Minha Localização",
                    isLoading = false
                )
            }
        }
    }

    private fun updateSuggestions(query: String) {
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            if (query.length > 2) {
                delay(500) // ⏳ Debounce para evitar chamadas excessivas na API
                _state.update { it.copy(isLoading = true) }

                searchRepository.getAutocomplete(query)
                    .onSuccess { list ->
                        _state.update { it.copy(suggestions = list, isLoading = false) }
                    }
                    .onFailure {
                        // Limpa a lista caso dê erro de internet ou API
                        _state.update { it.copy(suggestions = emptyList(), isLoading = false) }
                    }
            } else {
                _state.update { it.copy(suggestions = emptyList()) }
            }
        }
    }

    private fun getCoordinates(placeId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) } // Mostra loading ao clicar

            searchRepository.getPlaceCoordinates(placeId)
                .onSuccess { coordinate ->
                    _state.update { it.copy(isLoading = false) }
                    // Volta para a tela anterior com o dado real
                    _effect.send(SearchEffect.NavigateBackWithResult(coordinate))
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                    // Futuramente podemos enviar um efeito de erro aqui
                }
        }
    }
}