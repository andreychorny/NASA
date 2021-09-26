package com.example.nasa.di

import com.example.data.repository.RemoteSearchRepositoryImpl
import com.example.domain.repositories.RemoteSearchRepository
import com.example.nasa.rx.SchedulersFacade
import com.example.nasa.rx.SchedulersProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindSchedulerProvider(impl: SchedulersFacade): SchedulersProvider

    @Binds
    abstract fun provideRemoteSearchRepository(impl: RemoteSearchRepositoryImpl): RemoteSearchRepository

}