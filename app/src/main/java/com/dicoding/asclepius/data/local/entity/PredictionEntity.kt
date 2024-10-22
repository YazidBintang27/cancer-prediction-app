package com.dicoding.asclepius.data.local.entity

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prediction")
data class PredictionEntity(
   @PrimaryKey(autoGenerate = true)
   val id: Int? = null,

   @ColumnInfo(name = "image")
   var image: String? = "",

   @ColumnInfo(name = "result")
   var result: String? = "",

   @ColumnInfo(name = "confidence_score")
   var confidenceScore: Float? = 0.0f
)
