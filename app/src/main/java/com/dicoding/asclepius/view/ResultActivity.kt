package com.dicoding.asclepius.view

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.entity.PredictionEntity
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.viewmodel.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat

@AndroidEntryPoint
class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private val historyViewModel: HistoryViewModel by viewModels()
    private lateinit var predictionData: PredictionEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.result)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnBack.setOnClickListener { onBackPressed() }
        getData()
        binding.btnSave.setOnClickListener {
            saveData()
            onBackPressed()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getData() {
        val image = intent.getStringExtra("imageUri")
        val label = intent.getStringExtra("label")
        val score = intent.getFloatExtra("score", 0.0f)

        val confidenceScore = NumberFormat.getPercentInstance().format(score)

        binding.resultImage.setImageURI(Uri.parse(image))
        binding.resultText.text = "$label $confidenceScore"

        predictionData = PredictionEntity(
            image = image,
            result = label,
            confidenceScore = score
        )
    }

    private fun saveData() {
        lifecycleScope.launch(Dispatchers.IO) {
            historyViewModel.insertHistoryData(predictionData)
            Log.d("SaveData", "${predictionData.id}")
        }
        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show()
    }
}