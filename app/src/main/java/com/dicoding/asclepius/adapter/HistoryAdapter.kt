package com.dicoding.asclepius.adapter

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.entity.PredictionEntity
import com.dicoding.asclepius.databinding.ItemHistoryCardBinding
import java.text.NumberFormat

class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

   private var historyData: List<PredictionEntity> = listOf()

   class ViewHolder(
      private val binding: ItemHistoryCardBinding
   ): RecyclerView.ViewHolder(binding.root) {
      fun bind(data: PredictionEntity) {
         binding.apply {
            try {
               ivPredictionImage.setImageURI(Uri.parse(data.image))
            } catch (e: SecurityException) {
               Log.e("SecurityException", "${e.message}")
               ivPredictionImage.setImageResource(R.drawable.noimage)
            } finally {
               tvPredictionResult.text = data.result
               tvPredictionScore.text = NumberFormat.getPercentInstance().format(data.confidenceScore)
            }
         }
      }
   }

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val layoutInflater = LayoutInflater.from(parent.context)
      val binding = ItemHistoryCardBinding.inflate(layoutInflater, parent, false)
      return ViewHolder(binding)
   }

   override fun getItemCount(): Int {
      return historyData.size
   }

   override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      holder.bind(historyData[position])
   }

   @SuppressLint("NotifyDataSetChanged")
   fun setData(historyData: List<PredictionEntity>) {
      this.historyData = historyData
      notifyDataSetChanged()
   }
}