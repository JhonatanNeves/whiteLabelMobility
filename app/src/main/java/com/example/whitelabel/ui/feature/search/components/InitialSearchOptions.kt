package com.example.whitelabel.ui.feature.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.whitelabel.ui.feature.search.SearchEvent

@Composable
fun InitialSearchOptions(onEvent: (SearchEvent) -> Unit) {
    Column {
        ListItem(
            headlineContent = { Text("My favorites", fontWeight = FontWeight.Medium) },
            trailingContent = { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null) },
            modifier = Modifier.clickable { /* Dispare um evento aqui futuramente */ }
        )
        HorizontalDivider(thickness = 8.dp, color = Color(0xFFF3F3F3))

        ListItem(
            headlineContent = { Text("Choose on map", fontWeight = FontWeight.Medium) },
            leadingContent = { Icon(Icons.Default.PushPin, contentDescription = null, tint = Color(0xFF4A5568)) },
            modifier = Modifier.clickable { /* Dispare um evento aqui futuramente */ }
        )
        HorizontalDivider(thickness = 8.dp, color = Color(0xFFF3F3F3))
    }
}