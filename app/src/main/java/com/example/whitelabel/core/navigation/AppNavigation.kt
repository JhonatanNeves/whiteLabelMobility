package com.example.whitelabel.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.whitelabel.presentation.home.HomeScreen


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Home.path
    ) {

        composable(route = Route.Home.path) {
            HomeScreen(
                onNavigateToSearch = {
                    navController.navigate(Route.Search.path)
                }
            )
        }

        composable(route = Route.Search.path) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🚧 Tela de Busca em construção 🚧")
            }
        }
    }
}