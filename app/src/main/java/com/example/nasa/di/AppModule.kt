package com.example.nasa.di

import com.example.data.repository.AuthenticationRepositoryImpl
import com.example.data.repository.FirebasePostRepositoryImpl
import com.example.data.repository.FirebaseUserRepositoryImpl
import com.example.data.repository.RemoteSearchRepositoryImpl
import com.example.domain.repositories.AuthenticationRepository
import com.example.domain.repositories.FirebasePostRepository
import com.example.domain.repositories.FirebaseUserRepository
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

    @Binds
    abstract fun provideFirebaseUserRepository(impl: FirebaseUserRepositoryImpl): FirebaseUserRepository

    @Binds
    abstract fun provideFirebaseCommentRepository(impl: FirebasePostRepositoryImpl): FirebasePostRepository

    @Binds
    abstract fun provideAuthenticationRepository(impl: AuthenticationRepositoryImpl): AuthenticationRepository

}