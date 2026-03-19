package com.example.whitelabel.ui.feature.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whitelabel.ui.feature.search.SearchEvent

@Composable
fun SearchTopBar(
    originText: String,
    destinationQuery: String,
    onEvent: (SearchEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Voltar",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onEvent(SearchEvent.OnBackClick) },
                tint = Color(0xFF333333)
            )
            Text(
                text = "Choose your destination",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                ),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                color = Color(0xFF333333)
            )
            Spacer(modifier = Modifier.size(24.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.Top
        ) {
            TimelineIndicator()

            Column(modifier = Modifier.weight(1f)) {
                SearchInputField(
                    label = "Departure",
                    value = originText,
                    onValueChange = {},
                    trailingIcons = {
                        Icon(Icons.Default.StarBorder, contentDescription = null, tint = Color.Gray, modifier = Modifier.padding(8.dp))
                        Icon(Icons.Default.SwapVert, contentDescription = null, tint = Color.Gray, modifier = Modifier.padding(end = 8.dp))
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 1.dp, color = Color(0xFFF3F3F3))
                SearchInputField(
                    label = "Destination",
                    value = destinationQuery,
                    onValueChange = { onEvent(SearchEvent.OnQueryChange(it)) },
                    trailingIcons = {
                        Icon(Icons.Default.AddCircle, contentDescription = null, tint = Color(0xFF4A5568), modifier = Modifier.padding(end = 8.dp))
                    }
                )
            }
        }
        Box(modifier = Modifier.fillMaxWidth().height(8.dp).background(Color(0xFFF3F3F3)))
    }
}

@Composable
private fun SearchInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    trailingIcons: @Composable () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(bottom = 2.dp))
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                cursorBrush = SolidColor(Color.Black),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
        trailingIcons()
    }
}

@Composable
private fun TimelineIndicator() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 16.dp, end = 16.dp)
    ) {
        Box(modifier = Modifier.size(10.dp).background(Color(0xFF535860), CircleShape).padding(2.5.dp).background(Color.White, CircleShape))
        Box(modifier = Modifier.width(1.dp).height(46.dp).background(Color(0xFFE0E0E0)))
        Box(modifier = Modifier.size(10.dp).background(Color(0xFFF2C341), CircleShape).padding(2.5.dp).background(Color.White, CircleShape))
    }
}