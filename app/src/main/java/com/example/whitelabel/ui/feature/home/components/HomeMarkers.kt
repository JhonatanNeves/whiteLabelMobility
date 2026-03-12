package com.example.whitelabel.ui.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun OriginMarker(position: LatLng) {
    LocationMarker(
        position = position,
        label = "Sua localização",
        icon = Icons.Default.Person,
        iconBackgroundColor = Color(0xFF4A4E54)
    )
}

@Composable
fun DestinationMarker(position: LatLng, address: String, routeInfo: String? = null) {
    LocationMarker(
        position = position,
        label = address,
        icon = Icons.Default.Flag,
        iconBackgroundColor = Color(0xFFF3C111),
        infoTag = routeInfo
    )
}

@Composable
private fun LocationMarker(
    position: LatLng,
    label: String,
    icon: ImageVector,
    iconBackgroundColor: Color,
    infoTag: String? = null
) {
    MarkerComposable(state = rememberMarkerState(position = position)) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(iconBackgroundColor, CircleShape)
                    .border(2.dp, Color.White, CircleShape)
                    .offset(y = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = Color.White, modifier = Modifier.size(20.dp))
            }

            Spacer(modifier = Modifier.width(6.dp))

            Column {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White,
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.widthIn(max = 150.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.Edit, null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                    }
                }

                infoTag?.let {
                    Surface(
                        shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp),
                        color = Color(0xFF2C323A),
                        modifier = Modifier.padding(start = 12.dp)
                    ) {
                        Text(
                            text = it,
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}