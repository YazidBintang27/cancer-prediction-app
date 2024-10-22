package com.dicoding.asclepius.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dicoding.asclepius.data.local.entity.PredictionEntity

@Database(entities = [PredictionEntity::class], version = 1, exportSchema = false)
abstract class PredictionDatabase: RoomDatabase() {
   abstract val predictionDao: PredictionDao
}