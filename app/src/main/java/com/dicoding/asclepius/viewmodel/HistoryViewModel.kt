package com.dicoding.asclepius.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.local.entity.PredictionEntity
import com.dicoding.asclepius.data.remote.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
   private val repository: Repository
): ViewModel() {
   private var _historyData = MutableStateFlow<List<PredictionEntity>>(emptyList())
   val historyData: StateFlow<List<PredictionEntity>> = _historyData.asStateFlow()

   init {
      getAllHistoryData()
   }

   private fun getAllHistoryData() {
      viewModelScope.launch(Dispatchers.IO) {
         repository.getAllHistory().collect { data ->
            _historyData.value = data
         }
      }
   }

   fun insertHistoryData(predictionEntity: PredictionEntity) {
      viewModelScope.launch(Dispatchers.IO) {
         Log.d("HistoryViewModel", "Inserting data: $predictionEntity")
         repository.insertData(predictionEntity)
      }
   }
}