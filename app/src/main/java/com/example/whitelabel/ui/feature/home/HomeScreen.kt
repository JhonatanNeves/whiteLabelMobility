package com.example.whitelabel.ui.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.whitelabel.core.theme.WhiteLabelTheme
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
    onNavigateToSearch: () -> Unit
) {
    val cameraPositionState = rememberCameraPositionState()

    // 🔥 Aqui está a solução: A UI observa o state.userLocation que vem do Repository -> ViewModel
    LaunchedEffect(state.userLocation) {
        state.userLocation?.let { location ->
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(location, 15f),
                durationMs = 1500
            )
        }
    }

    BottomSheetScaffold(
        scaffoldState = rememberBottomSheetScaffoldState(),
        sheetPeekHeight = 200.dp,
        sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        sheetContainerColor = Color.White,
        sheetShadowElevation = 10.dp,
        sheetDragHandle = {
            // O "puxador" centralizado como na imagem
            BottomSheetDefaults.DragHandle(
                width = 40.dp,
                height = 4.dp,
                color = Color.LightGray,
            )
        },
        sheetContent = {
            // --- CONTEÚDO DO MENU (O que sobe e desce) ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 24.dp)
            ) {
                Text(
                    text = "Boa tarde, ${state.userName}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // BARRA DE BUSCA "PARA ONDE?"
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp)
                            .clip(RoundedCornerShape(27.dp))
                            .background(Color(0xFFF3F3F3)) // Cinza bem claro
                            .clickable { onNavigateToSearch() }
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Color.Black)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Para onde?", color = Color.DarkGray)
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // BOTÃO AGENDAR (CILINDRO VAZADO + CALENDÁRIO + RELÓGIO)
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, Color.LightGray.copy(alpha = 0.5f), CircleShape)
                            .clickable { onEvent(HomeEvent.OnScheduleClick) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(24.dp))
                        // Relógio atrelado no canto
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .offset(x = (-4).dp, y = (-4).dp)
                                .size(16.dp)
                                .background(Color.White, CircleShape)
                                .padding(1.dp)
                        ) {
                            Icon(Icons.Default.Schedule, contentDescription = null, tint = Color.Black)
                        }
                    }
                }

                // Espaço extra para quando o usuário puxar tudo para cima
                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = Color(0xFFF3F3F3))
                // Aqui você pode colocar sugestões de endereços favoritos no futuro
            }
        }

    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = state.userLocation != null),
                uiSettings = MapUiSettings(zoomControlsEnabled = false)
            )
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    WhiteLabelTheme {
        HomeScreen(
            state = HomeState(
                userName = "Usuário Teste",
                userLocation = LatLng(-23.5505, -46.6333), // Example location: São Paulo
                nearbyDrivers = listOf(
                    LatLng(-23.5510, -46.6340),
                    LatLng(-23.5490, -46.6320)
                )
            ),
            onEvent = {}, // Dummy lambda for preview
            onNavigateToSearch = {} // Dummy lambda for preview
        )
    }
}