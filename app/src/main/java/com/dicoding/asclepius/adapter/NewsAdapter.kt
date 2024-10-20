package com.dicoding.asclepius.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.remote.model.NewsResponse
import com.dicoding.asclepius.databinding.ItemNewsCardBinding
import java.text.SimpleDateFormat
import java.util.Locale

class NewsAdapter: RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

   private var newsData: List<NewsResponse.Article?>? = listOf()

   class ViewHolder(
      private val binding: ItemNewsCardBinding
   ): RecyclerView.ViewHolder(binding.root) {
      fun bind(data: NewsResponse.Article?) {
         val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
         val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
         val parsedDate = data?.publishedAt?.let { inputFormat.parse(it) }
         val formattedDate = parsedDate?.let { outputFormat.format(it) } ?: "Unknown Date"
         binding.apply {
            Glide.with(ivImageNews)
               .load(data?.urlToImage)
               .error(R.drawable.noimage)
               .into(ivImageNews)
            tvTitleNews.text = data?.title
            tvAuthorNews.text = data?.author.toString()
            tvDateNews.text = formattedDate
         }
      }
   }

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val layoutInflater = LayoutInflater.from(parent.context)
      val binding = ItemNewsCardBinding.inflate(layoutInflater, parent, false)
      return ViewHolder(binding)
   }

   override fun getItemCount(): Int {
      return newsData?.size ?: 0
   }

   override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      holder.bind(newsData?.get(position))
   }

   @SuppressLint("NotifyDataSetChanged")
   fun setData(newsData: List<NewsResponse.Article?>?) {
      this.newsData = newsData
      notifyDataSetChanged()
   }
}