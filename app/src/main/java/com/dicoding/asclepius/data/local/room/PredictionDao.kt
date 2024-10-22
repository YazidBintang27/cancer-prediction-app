package com.dicoding.asclepius.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dicoding.asclepius.data.local.entity.PredictionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PredictionDao {
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertPrediction(predictionEntity: PredictionEntity)

   @Update
   suspend fun updatePrediction(predictionEntity: PredictionEntity)

   @Delete
   suspend fun deletePrediction(predictionEntity: PredictionEntity)

   @Query("SELECT * FROM prediction")
   fun getAllPrediction(): Flow<List<PredictionEntity>>
}