package com.example.whitelabel.ui.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.whitelabel.core.theme.WhiteLabelTheme
import com.example.whitelabel.ui.feature.home.components.* // 🔥 Importa todos os seus componentes novos
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
    onNavigateToSearch: () -> Unit
) {
    // 1. Estados de Controle (Câmera e BottomSheet)
    val cameraPositionState = rememberCameraPositionState()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded
        )
    )

    // 2. Orquestração da Câmera (Reatividade MVI)
    // Voa para a localização do usuário ao abrir
    LaunchedEffect(state.userLocation) {
        state.userLocation?.let {
            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(it, 15f), 1000)
        }
    }

    // Voa para o destino quando selecionado
    LaunchedEffect(state.destinationLat, state.destinationLng) {
        if (state.destinationLat != null && state.destinationLng != null) {
            val dest = LatLng(state.destinationLat, state.destinationLng)
            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(dest, 16f), 1500)
        }
    }

    // 3. Estrutura Principal (Scaffold)
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 125.dp,
        sheetShape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp),
        sheetContainerColor = Color.White,
        sheetDragHandle = {
            // Pequena barra cinza de arrastar
            Box(modifier = Modifier.padding(vertical = 12.dp)) {
                Surface(
                    modifier = Modifier.width(32.dp).height(4.dp),
                    color = Color.LightGray,
                    shape = RoundedCornerShape(2.dp)
                ) {}
            }
        },
        sheetContent = {
            // Componente isolado da gaveta
            HomeBottomSheetContent(
                userName = state.userName,
                destinationAddress = state.destinationAddress,
                onSearchClick = onNavigateToSearch,
                onScheduleClick = { onEvent(HomeEvent.OnScheduleClick) }
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            // Componente isolado do Mapa
            HomeMap(
                state = state,
                cameraPositionState = cameraPositionState
            )

            //  Componente isolado da Barra Superior
            HomeTopBar(
                appName = "MoVee"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    WhiteLabelTheme {
        HomeScreen(
            state = HomeState(userName = "Jhonatan"),
            onEvent = {},
            onNavigateToSearch = {}
        )
    }
}