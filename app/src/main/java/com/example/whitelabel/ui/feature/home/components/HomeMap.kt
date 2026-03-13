package com.example.whitelabel.ui.feature.home.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.whitelabel.ui.feature.home.HomeState
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.RoundCap
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline

@Composable
fun HomeMap(
    state: HomeState,
    cameraPositionState: CameraPositionState
) {
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = state.userLocation != null),
        uiSettings = MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false),
        contentPadding = PaddingValues(bottom = 140.dp, top = 100.dp)
    ) {
        // Desenha a linha
        if (state.routePolylines.isNotEmpty()) {
            Polyline(
                points = state.routePolylines,
                color = Color(0xFF4A89F3),
                width = 12f,
                // 🔥 ISSO AQUI MUDA TUDO:
                jointType = JointType.ROUND, // Arredonda as esquinas das ruas
                startCap = RoundCap(),       // Arredonda o início da linha
                endCap = RoundCap()          // Arredonda o fim da linha
            )
        }

        // Marcador de Origem (Apenas ícone e label fixo)
        state.userLocation?.let { origin ->
            if (state.destinationLat != null) {
                OriginMarker(position = origin)
            }
        }

        //  MARCADOR DE DESTINO REFATORADO
        if (state.destinationLat != null && state.destinationLng != null) {
            DestinationMarker(
                position = LatLng(state.destinationLat, state.destinationLng),
                address = state.destinationAddress ?: "Destino",
                routeInfo = when {
                    // 1. Se temos o dado real, mostramos o melhor cenário
                    state.distance != null -> "${state.distance} • ${state.duration}"

                    // 2. Se está carregando (precisa ter o isLoading no HomeState)
                    state.isLoading -> "Calculando rota..."

                    // 3. Se o usuário escolheu um destino, mas a lista de pontos continua vazia após o cálculo
                    state.destinationLat != null && state.routePolylines.isEmpty() -> "Sem rota disponível"

                    // 4. Caso contrário (tela inicial ou limpando busca), não mostra nada
                    else -> null
                }
            )
        }
    }
}