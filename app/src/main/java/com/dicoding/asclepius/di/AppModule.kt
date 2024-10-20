package com.dicoding.asclepius.di

import com.dicoding.asclepius.data.remote.service.NewsService
import com.dicoding.asclepius.helper.ApiHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

   @Provides
   @Singleton
   fun provideRetrofit(): Retrofit {
      return Retrofit.Builder()
         .baseUrl(ApiHelper.BASE_URL)
         .addConverterFactory(GsonConverterFactory.create())
         .build()
   }

   @Provides
   @Singleton
   fun provideNewsService(retrofit: Retrofit): NewsService {
      return retrofit.create(NewsService::class.java)
   }
}