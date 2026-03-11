package com.example.whitelabel.ui.feature.main

sealed interface MainEvent {
    data class OnTabSelected(val index: Int) : MainEvent
}