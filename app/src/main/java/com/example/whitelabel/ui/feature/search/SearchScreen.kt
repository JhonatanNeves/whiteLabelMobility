package com.example.whitelabel.ui.feature.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.whitelabel.ui.feature.search.components.InitialSearchOptions
import com.example.whitelabel.ui.feature.search.components.SearchResultsList
import com.example.whitelabel.ui.feature.search.components.SearchTopBar

@Composable
fun SearchScreen(
    state: SearchState,
    onEvent: (SearchEvent) -> Unit
) {
    Scaffold(
        topBar = {
            SearchTopBar(
                originText = state.originAddress,
                destinationQuery = state.destinationQuery,
                onEvent = onEvent
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White)
        ) {
            // A única "lógica" permitida aqui é o roteamento visual baseado no Estado MVI
            if (state.suggestions.isEmpty() && state.destinationQuery.isBlank()) {
                InitialSearchOptions(onEvent = onEvent)
            } else {
                SearchResultsList(
                    suggestions = state.suggestions,
                    onSuggestionClick = { onEvent(SearchEvent.OnSuggestionSelected(it)) }
                )
            }
        }
    }
}