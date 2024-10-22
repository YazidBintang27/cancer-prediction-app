package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.adapter.NewsAdapter
import com.dicoding.asclepius.databinding.ActivityNewsBinding
import com.dicoding.asclepius.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsActivity : AppCompatActivity() {

   private lateinit var binding: ActivityNewsBinding
   private val newsAdapter: NewsAdapter by lazy { NewsAdapter() }
   private val newsViewModel: NewsViewModel by viewModels()

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      enableEdgeToEdge()
      binding = ActivityNewsBinding.inflate(layoutInflater)
      setContentView(binding.root)
      ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
         val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
         v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
         insets
      }
      binding.newsToolbar.setNavigationOnClickListener { onBackPressed() }
      setupAdapter()
      getData()
      openNewsDetail()
   }

   private fun setupAdapter() {
      binding.rvNewsList.apply {
         adapter = newsAdapter
         layoutManager = LinearLayoutManager(this@NewsActivity, LinearLayoutManager.VERTICAL, false)
         setHasFixedSize(true)
      }
   }

   private fun getData() {
      lifecycleScope.launch {
         repeatOnLifecycle(Lifecycle.State.STARTED) {
            newsViewModel.isLoading.collect { isLoading ->
               binding.progressIndicator.visibility = if (isLoading == true) View.VISIBLE else View.GONE
            }
         }
      }

      lifecycleScope.launch {
         repeatOnLifecycle(Lifecycle.State.STARTED) {
            newsViewModel.newsData.collect { newsList ->
               Log.d("CheckDataNews", "$newsList")
               newsAdapter.setData(newsList)
            }
         }
      }
   }

   private fun openNewsDetail() {
      newsAdapter.setOnClickCallback(object: NewsAdapter.OnItemClickCallback {
         override fun onItemClicked(index: Int) {
            lifecycleScope.launch {
               newsViewModel.newsData.collect { news ->
                  startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(news?.get(index)?.url)))
               }
            }
         }
      })
   }
}