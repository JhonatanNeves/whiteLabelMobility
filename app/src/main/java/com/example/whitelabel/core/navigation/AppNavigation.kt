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

import com.example.whitelabel.ui.feature.main.MainScreen
import com.example.whitelabel.ui.feature.main.MainViewModel
import com.example.whitelabel.ui.feature.search.SearchScreen
import com.example.whitelabel.ui.feature.search.SearchViewModel
import com.example.whitelabel.ui.feature.search.SearchEffect

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.HomeRoute,
        modifier = modifier
    ) {

        composable<Route.HomeRoute> {
            val mainViewModel: MainViewModel = hiltViewModel()
            val mainState by mainViewModel.state.collectAsState()

            MainScreen(
                state = mainState,
                onEvent = mainViewModel::onEvent,
                onNavigateToSearch = {
                    navController.navigate(Route.SearchRoute)
                }
            )
        }

        composable<Route.SearchRoute> {
            val searchViewModel: SearchViewModel = hiltViewModel()
            val searchState by searchViewModel.state.collectAsState()

            LaunchedEffect(Unit) {
                searchViewModel.effect.collect { effect ->
                    when (effect) {
                        is SearchEffect.NavigateBack -> {
                            navController.navigateUp()
                        }

                        else -> {}
                    }
                }
            }

            SearchScreen(
                state = searchState,
                onEvent = searchViewModel::onEvent
            )
        }
    }
}