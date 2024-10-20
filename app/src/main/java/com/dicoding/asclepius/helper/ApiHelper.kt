package com.dicoding.asclepius.helper

import com.dicoding.asclepius.BuildConfig

object ApiHelper {
   const val BASE_URL = "https://newsapi.org/v2/"
   const val TOP_HEADLINE_ENDPOINT = "top-headlines"
   const val API_KEY = BuildConfig.NEWS_API_KEY
   const val Q = "cancer"
   const val COUNTRY = "us"
   const val CATEGORY = "health"
}