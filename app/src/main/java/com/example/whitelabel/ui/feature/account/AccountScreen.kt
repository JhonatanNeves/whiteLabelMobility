package com.example.whitelabel.ui.feature.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AccountScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text("Sua Conta", style = MaterialTheme.typography.headlineMedium)
        // Aqui virá foto, nome, configurações...
    }
}

@Preview
@Composable
private fun AccountScreenPreview() {
    AccountScreen()

}