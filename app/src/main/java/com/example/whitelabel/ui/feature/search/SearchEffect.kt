package com.example.whitelabel.ui.feature.search

import com.example.whitelabel.domain.model.LocationCoordinate

sealed interface SearchEffect {
    data object NavigateBack : SearchEffect

    data class NavigateBackWithResult(val coordinate: LocationCoordinate) : SearchEffect
}