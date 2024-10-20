package com.dicoding.asclepius.data.remote.repository

import com.dicoding.asclepius.data.remote.model.NewsResponse

interface NewsRepository {
   suspend fun getAllNews(
      q: String?,
      country: String?,
      category: String?,
      apiKey: String?
   ): NewsResponse
}