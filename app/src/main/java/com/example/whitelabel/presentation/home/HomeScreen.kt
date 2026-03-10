package com.example.whitelabel.presentation.home

import com.google.android.gms.maps.CameraUpdateFactory
import android.Manifest
import android.R.attr.bottom
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whitelabel.core.theme.WhiteLabelTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.ui.draw.clip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
    onNavigateToSearch: () -> Unit
) {

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded
        )
    )

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
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
        // --- CONTEÚDO DO FUNDO (MAPA E BOTÕES FLUTUANTES) ---
        Box(modifier = Modifier.fillMaxSize()) {

            // MAPA (Sempre no fundo)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(state.userLocation ?: LatLng(0.0, 0.0), 15f)
            }
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = state.userLocation != null),
                uiSettings = MapUiSettings(zoomControlsEnabled = false)
            )

            // UI SUPERIOR (BOTÕES FLUTUANTES)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 60.dp) // Ajuste para não bater no entalhe (notch)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    shape = RoundedCornerShape(26.dp),
                    color = Color(0xFFF3C111),
                    shadowElevation = 6.dp
                ) {
                    Text(
                        text = "Moto SJ",
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
                        fontWeight = FontWeight.W500,
                        color = Color.Black
                    )
                }

                // Botão Notificação com Badge (Ponto Vermelho)
                Box {
                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 6.dp,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = null,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                    // Ponto Vermelho de Alerta
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(Color.Red, CircleShape)
                            .align(Alignment.TopEnd)
                            .border(2.dp, Color.White, CircleShape)
                    )
                }
            }
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


