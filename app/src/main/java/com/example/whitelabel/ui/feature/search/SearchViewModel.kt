package com.example.whitelabel.ui.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private val _effect = Channel<SearchEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnOriginChanged -> {
                _state.update { it.copy(originQuery = event.query) }
            }
            is SearchEvent.OnDestinationChanged -> {
                _state.update { it.copy(destinationQuery = event.query) }
                updateSuggestions(event.query) // Simula busca
            }
            is SearchEvent.OnBackClick -> {
                viewModelScope.launch { _effect.send(SearchEffect.NavigateBack) }
            }
            is SearchEvent.OnSuggestionSelected -> {
                // Lógica de seleção
            }
        }
    }

    private fun updateSuggestions(query: String) {
        if (query.length > 2) {
            _state.update { it.copy(suggestions = listOf("Av. Paulista, 1000", "Rua Augusta, 500", "Aeroporto de Congonhas")) }
        } else {
            _state.update { it.copy(suggestions = emptyList()) }
        }
    }
}