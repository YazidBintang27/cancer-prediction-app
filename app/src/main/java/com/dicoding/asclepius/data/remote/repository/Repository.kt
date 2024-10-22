package com.dicoding.asclepius.data.remote.repository

import com.dicoding.asclepius.data.local.entity.PredictionEntity
import com.dicoding.asclepius.data.remote.model.NewsResponse
import kotlinx.coroutines.flow.Flow

interface Repository {
   suspend fun getAllNews(
      q: String?,
      country: String?,
      category: String?,
      apiKey: String?
   ): NewsResponse

   suspend fun getAllHistory(): Flow<List<PredictionEntity>>

   suspend fun insertData(predictionEntity: PredictionEntity)

   suspend fun updateData(predictionEntity: PredictionEntity)

   suspend fun deleteData(predictionEntity: PredictionEntity)
}