package com.example.whitelabel.presentation.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.whitelabel.core.theme.WhiteLabelTheme // Assuming you have a theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    state: SearchState,
    onEvent: (SearchEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Destination") },
                navigationIcon = {
                    IconButton(onClick = { onEvent(SearchEvent.OnBackPress) }) { // Assuming an OnBackPress event
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { query -> onEvent(SearchEvent.OnQueryChanged(query)) },
                label = { Text("Enter destination") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search Icon")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                trailingIcon = {
                    if (state.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onEvent(SearchEvent.OnQueryChanged("")) }) { // Clear search
                            Icon(Icons.Default.ArrowBack, contentDescription = "Clear Search") // Using ArrowBack as a placeholder for clear icon
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Add a Search Button
            Button(
                onClick = { onEvent(SearchEvent.OnSearch) },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.searchQuery.isNotBlank() // Enable button only if query is not blank
            ) {
                Text("Search")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (state.error != null) {
                Text("Error: ${state.error}", color = MaterialTheme.colorScheme.error)
            } else if (state.searchResults.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(state.searchResults) { result ->
                        Text(
                            text = result,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                                .clickable {
                                    // Handle selection of a search result, e.g.,
                                    // onEvent(SearchEvent.OnLocationSelected(result))
                                },
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Divider()
                    }
                }
            } else if (state.searchQuery.isNotBlank()) { // Changed to isNotBlank() for consistency
                Text("No results found for \"${state.searchQuery}\"")
            }
        }
    }
}

// Preview function
@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    WhiteLabelTheme {
        SearchScreen(
            state = SearchState(
                searchQuery = "Eiffel Tower",
                searchResults = listOf("Paris, France", "Eiffel Tower Restaurant"),
                isLoading = false
            ),
            onEvent = { /* Do nothing for preview */ },
            modifier = Modifier
        )
    }
}
