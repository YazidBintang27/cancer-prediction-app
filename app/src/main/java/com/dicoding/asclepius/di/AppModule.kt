package com.dicoding.asclepius.di

import android.app.Application
import androidx.room.Room
import com.dicoding.asclepius.data.local.room.PredictionDao
import com.dicoding.asclepius.data.local.room.PredictionDatabase
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

   @Provides
   @Singleton
   fun provideDatabase(app: Application): PredictionDatabase {
      return Room.databaseBuilder(
         app,
         PredictionDatabase::class.java,
         "prediction.db"
      ).build()
   }

   @Provides
   @Singleton
   fun provideDao(db: PredictionDatabase): PredictionDao {
      return db.predictionDao
   }
}