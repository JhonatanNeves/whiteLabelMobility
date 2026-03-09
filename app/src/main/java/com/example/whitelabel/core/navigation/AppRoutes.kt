package com.example.whitelabel.core.navigation

sealed class Route(val path: String) {
    data object Home : Route("home_screen")
    data object Search : Route("search_screen")
}