package com.dicoding.asclepius.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.remote.model.NewsResponse
import com.dicoding.asclepius.data.remote.repository.NewsRepository
import com.dicoding.asclepius.helper.ApiHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
   private val newsRepository: NewsRepository
): ViewModel() {
   private var _isLoading = MutableStateFlow<Boolean?>(null)
   val isLoading: StateFlow<Boolean?> = _isLoading.asStateFlow()

   private var _newsData = MutableStateFlow<List<NewsResponse.Article?>?>(null)
   val newsData: StateFlow<List<NewsResponse.Article?>?> = _newsData.asStateFlow()

   init {
      getAllNewsData()
   }

   private fun getAllNewsData() {
      viewModelScope.launch {
         _isLoading.value = true
         try {
            val data = newsRepository.getAllNews(
               ApiHelper.Q,
               ApiHelper.COUNTRY,
               ApiHelper.CATEGORY,
               ApiHelper.API_KEY
            ).articles
            _newsData.value = data
            Log.d("CheckDataNews", "data in viewmodel: $data")
            _isLoading.value = false
         } catch (e: Exception) {
            Log.e("NewsViewModel", "ERROR: ${e.message}")
         }
      }
   }
}