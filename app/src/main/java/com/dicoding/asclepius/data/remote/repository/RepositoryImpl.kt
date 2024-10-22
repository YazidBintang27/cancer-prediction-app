package com.dicoding.asclepius.data.remote.repository

import com.dicoding.asclepius.data.local.entity.PredictionEntity
import com.dicoding.asclepius.data.local.room.PredictionDao
import com.dicoding.asclepius.data.remote.model.NewsResponse
import com.dicoding.asclepius.data.remote.service.NewsService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
   private val newsService: NewsService,
   private val predictionDao: PredictionDao
): Repository {
   override suspend fun getAllNews(q: String?, country: String?, category: String?, apiKey: String?): NewsResponse {
      return newsService.getAllNews(q, country, category, apiKey)
   }

   // Room database
   override suspend fun getAllHistory(): Flow<List<PredictionEntity>> {
      return predictionDao.getAllPrediction()
   }

   override suspend fun insertData(predictionEntity: PredictionEntity) {
      predictionDao.insertPrediction(predictionEntity)
   }

   override suspend fun updateData(predictionEntity: PredictionEntity) {
      predictionDao.updatePrediction(predictionEntity)
   }

   override suspend fun deleteData(predictionEntity: PredictionEntity) {
      predictionDao.deletePrediction(predictionEntity)
   }
}