package com.example.whitelabel.ui.feature.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    state: SearchState,
    onEvent: (SearchEvent) -> Unit
) {
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding() // Protege contra a barra transparente
                    .padding(16.dp)
            ) {
                // Botão Voltar
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Voltar",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onEvent(SearchEvent.OnBackClick) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(24.dp)
                    ) {
                        Box(modifier = Modifier.size(8.dp).background(Color.LightGray, CircleShape))
                        Box(modifier = Modifier.width(1.dp).height(40.dp).background(Color.LightGray))
                        Box(modifier = Modifier.size(8.dp).background(Color.Black))
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Os campos de texto
                    Column(modifier = Modifier.weight(1f)) {
                        BasicTextField(
                            value = state.originQuery,
                            onValueChange = { onEvent(SearchEvent.OnOriginChanged(it)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(36.dp)
                                .background(Color(0xFFF3F3F3), RoundedCornerShape(4.dp))
                                .padding(8.dp),
                            singleLine = true,
                            textStyle = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        BasicTextField(
                            value = state.destinationQuery,
                            onValueChange = { onEvent(SearchEvent.OnDestinationChanged(it)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(36.dp)
                                .background(Color(0xFFE8E8E8), RoundedCornerShape(4.dp))
                                .padding(8.dp),
                            singleLine = true,
                            textStyle = MaterialTheme.typography.bodyMedium,
                            decorationBox = { innerTextField ->
                                if (state.destinationQuery.isEmpty()) {
                                    Text("Para onde?", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
                                }
                                innerTextField()
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White)
        ) {
            items(state.suggestions) { suggestion ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onEvent(SearchEvent.OnSuggestionSelected(suggestion)) }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = suggestion.primaryText,
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = suggestion.secondaryText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 0.5.dp,
                    color = Color.LightGray
                )
            }
        }
    }
}