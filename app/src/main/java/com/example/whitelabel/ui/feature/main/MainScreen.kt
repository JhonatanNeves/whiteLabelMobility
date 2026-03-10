package com.example.whitelabel.ui.feature.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.whitelabel.presentation.account.AccountScreen
import com.example.whitelabel.presentation.activity.ActivityScreen
import com.example.whitelabel.ui.feature.home.HomeScreen
import com.example.whitelabel.ui.feature.home.HomeViewModel

@Composable
fun MainScreen(
    onNavigateToSearch: () -> Unit
) {

    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    label = { Text("Home") },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    label = { Text("Atividade") },
                    icon = { Icon(Icons.Default.History, contentDescription = null) }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    label = { Text("Conta") },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedTab) {
                0 -> {
                    val homeViewModel: HomeViewModel = hiltViewModel()
                    val state by homeViewModel.state.collectAsState()

                    HomeScreen(
                        state = state,
                        onEvent = homeViewModel::onEvent,
                        onNavigateToSearch = onNavigateToSearch
                    )
                }
                1 -> ActivityScreen()
                2 -> AccountScreen()
            }
        }
    }
}