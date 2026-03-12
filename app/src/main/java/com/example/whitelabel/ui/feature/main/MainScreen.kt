package com.example.whitelabel.ui.feature.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.whitelabel.ui.feature.home.HomeEvent
import com.example.whitelabel.ui.feature.home.HomeScreen
import com.example.whitelabel.ui.feature.home.HomeViewModel

@Composable
fun MainScreen(
    state: MainState,
    onEvent: (MainEvent) -> Unit,
    onNavigateToSearch: () -> Unit,
    destLat: Double? = null,
    destLng: Double? = null,
    destAddress: String? = null,
    onDestinationConsumed: () -> Unit = {}
) {
    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp),
                containerColor = Color.White,
                tonalElevation = 0.dp,
                windowInsets = WindowInsets.navigationBars
            ) {

                NavigationBarItem(
                    selected = state.selectedTabIndex == 0,
                    onClick = { onEvent(MainEvent.OnTabSelected(0)) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color(0xFFE0AB00)
                    )
                )

                NavigationBarItem(
                    selected = state.selectedTabIndex == 1,
                    onClick = { onEvent(MainEvent.OnTabSelected(1)) },
                    icon = { Icon(Icons.Default.History, contentDescription = "Activity") },
                    label = { Text("Activity") }
                )

                NavigationBarItem(
                    selected = state.selectedTabIndex == 2,
                    onClick = { onEvent(MainEvent.OnTabSelected(2)) },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Account") },
                    label = { Text("Account") }
                )
            }
        }
    ) { paddingValues ->

        Box(modifier = Modifier
            .padding(bottom = paddingValues.calculateBottomPadding())
            .fillMaxSize()) {
            when (state.selectedTabIndex) {
                0 -> {

                    val homeViewModel: HomeViewModel = hiltViewModel()
                    val homeState by homeViewModel.state.collectAsState()

                    LaunchedEffect(destLat, destLng) {
                        if (destLat != null && destLng != null && destAddress != null) {
                            homeViewModel.onEvent(
                                HomeEvent.OnDestinationSelected(destLat, destLng, destAddress)
                            )
                            onDestinationConsumed()
                        }
                    }

                    HomeScreen(
                        state = homeState,
                        onEvent = homeViewModel::onEvent,
                        onNavigateToSearch = onNavigateToSearch
                    )
                }
                1 -> {

                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("🚧 Tela de Atividades em construção 🚧")
                    }
                }
                2 -> {

                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("🚧 Tela de Conta em construção 🚧")
                    }
                }
            }
        }
    }
}