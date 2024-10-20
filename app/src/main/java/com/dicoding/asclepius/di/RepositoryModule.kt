package com.dicoding.asclepius.di

import com.dicoding.asclepius.data.remote.repository.NewsRepository
import com.dicoding.asclepius.data.remote.repository.NewsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {
   @Binds
   @Singleton
   abstract fun bindRepository(newsRepositoryImpl: NewsRepositoryImpl): NewsRepository
}