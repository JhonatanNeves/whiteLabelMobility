package com.example.whitelabel.presentation.search

sealed class SearchEffect {
    data object NavigateBack : SearchEffect()
}
