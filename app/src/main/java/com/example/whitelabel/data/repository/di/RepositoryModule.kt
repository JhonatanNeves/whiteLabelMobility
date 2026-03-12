package com.example.whitelabel.data.repository.di

import com.example.whitelabel.data.repository.DirectionsRepositoryImpl
import com.example.whitelabel.data.repository.LocationRepository
import com.example.whitelabel.data.repository.LocationRepositoryImpl
import com.example.whitelabel.domain.repository.DirectionsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLocationRepository(
        impl: LocationRepositoryImpl
    ): LocationRepository

    @Binds
    @Singleton
    abstract fun bindDirectionsRepository(
        impl: DirectionsRepositoryImpl
    ): DirectionsRepository
}