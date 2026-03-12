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
import com.example.whitelabel.domain.repository.SearchRepository

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private val _effect = Channel<SearchEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnOriginChanged -> {
                _state.update { it.copy(originQuery = event.query) }
                searchPlaces(event.query)
            }
            is SearchEvent.OnDestinationChanged -> {
                _state.update { it.copy(destinationQuery = event.query) }
                searchPlaces(event.query)
            }
            is SearchEvent.OnBackClick -> {
                viewModelScope.launch { _effect.send(SearchEffect.NavigateBack) }
            }
            is SearchEvent.OnSuggestionSelected -> {
                getCoordinates(event.suggestion.placeId)
            }
        }
    }
    private fun getCoordinates(placeId: String) {
        viewModelScope.launch {
            searchRepository.getPlaceCoordinates(placeId).onSuccess { coordinate ->
                _effect.send(SearchEffect.NavigateBackWithResult(coordinate))
            }.onFailure {
            }
        }
    }

    private fun searchPlaces(query: String) {
        viewModelScope.launch {
            if (query.length > 2) {
                searchRepository.getAutocomplete(query).onSuccess { results ->
                    _state.update { it.copy(suggestions = results) }
                }.onFailure {
                    _state.update { it.copy(suggestions = emptyList()) }
                }
            } else {
                _state.update { it.copy(suggestions = emptyList()) }
            }
        }
    }
}