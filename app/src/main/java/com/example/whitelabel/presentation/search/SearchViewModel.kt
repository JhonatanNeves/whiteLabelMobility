package com.example.whitelabel.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whitelabel.presentation.search.SearchEvent
import com.example.whitelabel.presentation.search.SearchState
import com.example.whitelabel.presentation.search.SearchEffect // Make sure this is imported
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
class SearchViewModel @Inject constructor(
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private val _effect = Channel<SearchEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnQueryChanged -> {
                _state.update { it.copy(searchQuery = event.query) }
            }
            is SearchEvent.OnSearch -> {

                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true, error = null) } // Show loading
                    try {

                        _state.update {
                            it.copy(
                                searchResults = listOf("Result 1 for ${it.searchQuery}", "Result 2 for ${it.searchQuery}"),
                                isLoading = false
                            )
                        }

                    } catch (e: Exception) {
                        _state.update { it.copy(error = e.message, isLoading = false) }
                    }
                }
            }
            is SearchEvent.OnBackPress -> {
                viewModelScope.launch { _effect.send(SearchEffect.NavigateBack) }
            }
        }
    }
}
