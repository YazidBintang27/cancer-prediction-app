package com.dicoding.asclepius.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.adapter.HistoryAdapter
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.viewmodel.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryActivity : AppCompatActivity() {

   private lateinit var binding: ActivityHistoryBinding
   private val historyAdapter: HistoryAdapter by lazy { HistoryAdapter() }
   private val historyViewModel: HistoryViewModel by viewModels()

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      enableEdgeToEdge()
      binding = ActivityHistoryBinding.inflate(layoutInflater)
      setContentView(binding.root)
      ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.history)) { v, insets ->
         val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
         v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
         insets
      }
      binding.historyToolbar.setNavigationOnClickListener { onBackPressed() }
      setupAdapter()
      getData()
   }

   private fun setupAdapter() {
      binding.rvHistoryList.apply {
         adapter = historyAdapter
         layoutManager = GridLayoutManager(this@HistoryActivity, 2)
         setHasFixedSize(true)
      }
   }

   private fun getData() {
      lifecycleScope.launch {
         historyViewModel.historyData.collect {
            Log.d("HistoryActivity", "$it")
            historyAdapter.setData(it)
         }
      }
   }
}