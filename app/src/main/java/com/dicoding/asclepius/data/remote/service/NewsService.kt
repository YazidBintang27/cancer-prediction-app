package com.dicoding.asclepius.data.remote.service

import com.dicoding.asclepius.data.remote.model.NewsResponse
import com.dicoding.asclepius.helper.ApiHelper
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {
   @GET(ApiHelper.TOP_HEADLINE_ENDPOINT)
   suspend fun getAllNews(
      @Query("q") q: String?,
      @Query("country") country: String?,
      @Query("category") category: String?,
      @Query("apikey") apiKey: String?
   ): NewsResponse
}