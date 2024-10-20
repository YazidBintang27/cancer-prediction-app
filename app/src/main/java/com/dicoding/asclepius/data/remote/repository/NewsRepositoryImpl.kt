package com.dicoding.asclepius.data.remote.repository

import com.dicoding.asclepius.data.remote.model.NewsResponse
import com.dicoding.asclepius.data.remote.service.NewsService
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
   private val newsService: NewsService
): NewsRepository {
   override suspend fun getAllNews(q: String?, country: String?, category: String?, apiKey: String?): NewsResponse {
      return newsService.getAllNews(q, country, category, apiKey)
   }
}