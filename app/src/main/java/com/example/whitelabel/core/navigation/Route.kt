package com.example.whitelabel.core.navigation

import kotlinx.serialization.Serializable



sealed interface Route {

    @Serializable
    data object HomeRoute : Route

    @Serializable
    data object SearchRoute

}
