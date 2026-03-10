package com.example.whitelabel.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.whitelabel.presentation.home.HomeScreen
import com.example.whitelabel.presentation.home.HomeViewModel

import com.example.whitelabel.presentation.search.SearchScreen
import com.example.whitelabel.presentation.search.SearchViewModel
import com.example.whitelabel.presentation.search.SearchEffect

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.HomeRoute, // Chamando o objeto que está dentro de Route
        modifier = modifier
    ) {

        // --- TELA HOME (Herda de Route) ---
        composable<Route.HomeRoute> {
            val homeViewModel: HomeViewModel = hiltViewModel()
            val homeState by homeViewModel.state.collectAsState()

            HomeScreen(
                state = homeState,
                onEvent = { event -> homeViewModel.onEvent(event) },
                onNavigateToSearch = {
                    // Navegando para o objeto SearchRoute (que não herda de Route, mas está no mesmo arquivo)
                    navController.navigate(Route.SearchRoute)
                }
            )
        }

        // --- TELA SEARCH (NÃO herda de Route, mas é acessada via Route.SearchRoute) ---
        composable<Route.SearchRoute> {
            val searchViewModel: SearchViewModel = hiltViewModel()
            val searchState by searchViewModel.state.collectAsState()

            LaunchedEffect(Unit) {
                searchViewModel.effect.collect { effect ->
                    when (effect) {
                        is SearchEffect.NavigateBack -> {
                            navController.navigateUp()
                        }
                    }
                }
            }

            SearchScreen(
                state = searchState,
                onEvent = { event -> searchViewModel.onEvent(event) }
            )
        }
    }
}