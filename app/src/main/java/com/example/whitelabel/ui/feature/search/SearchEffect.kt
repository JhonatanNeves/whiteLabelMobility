package com.example.whitelabel.ui.feature.search

sealed interface SearchEffect {
    data object NavigateBack : SearchEffect
}