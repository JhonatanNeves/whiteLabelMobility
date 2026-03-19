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
import com.example.whitelabel.domain.model.LocationCoordinate
import com.example.whitelabel.ui.feature.main.MainEvent
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

        composable<Route.HomeRoute> { backStackEntry ->
            val mainViewModel: MainViewModel = hiltViewModel()
            val mainState by mainViewModel.state.collectAsState()

            // 1. Pegamos o SavedStateHandle da tela ATUAL (Home)
            val savedStateHandle = backStackEntry.savedStateHandle

            // 2. Observamos a chave correta: "selected_location" (que vem da Search)
            // Usamos collectAsState para que o Compose reaja quando o valor mudar
            val selectedLocation by savedStateHandle
                .getStateFlow<LocationCoordinate?>("selected_location", null)
                .collectAsState()

            // 3. Quando o local selecionado chegar, avisamos o ViewModel
            LaunchedEffect(selectedLocation) {
                selectedLocation?.let { coordinate ->
                    println("CHEGOU NA HOME: ${coordinate.address} (${coordinate.latitude}, ${coordinate.longitude})")

                    // DESCOMENTADO E CORRIGIDO:
                    mainViewModel.onEvent(
                        MainEvent.OnDestinationSelected(
                        latitude = coordinate.latitude,
                        longitude = coordinate.longitude,
                        address = coordinate.address
                    ))

                    // 4. Limpamos a "mochila" para não processar o mesmo destino duas vezes (ex: ao girar a tela)
                    savedStateHandle.remove<LocationCoordinate>("selected_location")
                }
            }

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
                            navController.popBackStack()
                        }
                        is SearchEffect.NavigateBackWithResult -> {
                            // 🔥 O segredo: a chave aqui deve ser IGUAL à que a Home está ouvindo
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("selected_location", effect.coordinate)

                            navController.popBackStack()
                        }
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