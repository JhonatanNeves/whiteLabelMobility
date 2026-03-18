package com.example.whitelabel.di // Na raiz do DI

import com.example.whitelabel.data.repository.DirectionsRepositoryImpl
import com.example.whitelabel.data.repository.LocationRepositoryImpl
import com.example.whitelabel.data.repository.SearchRepositoryImpl
import com.example.whitelabel.domain.repository.DirectionsRepository
import com.example.whitelabel.domain.repository.LocationRepository
import com.example.whitelabel.domain.repository.SearchRepository
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
    abstract fun bindSearchRepository(
        searchRepositoryImpl: SearchRepositoryImpl
    ): SearchRepository

    @Binds
    @Singleton
    abstract fun bindDirectionsRepository(
        impl: DirectionsRepositoryImpl
    ): DirectionsRepository

    @Binds
    @Singleton
    abstract fun bindLocationRepository(
        impl: LocationRepositoryImpl
    ): LocationRepository
}