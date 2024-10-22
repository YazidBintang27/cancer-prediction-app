package com.dicoding.asclepius.view

import android.content.Intent
import android.Manifest
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.yalantis.ucrop.UCrop
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri? = null
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.history -> {
                    startActivity(Intent(this@MainActivity, HistoryActivity::class.java))
                    true
                }
                R.id.news -> {
                    startActivity(Intent(this@MainActivity, NewsActivity::class.java))
                    true
                }
                else -> false
            }
        }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener { analyzeImage() }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri: Uri? = UCrop.getOutput(data!!)
            resultUri?.let {
                currentImageUri = it
                showImage()
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            cropError?.let { Log.e("UCropError", it.message.toString()) }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            cropImage(uri)
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("ImageURI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun cropImage(sourceUri: Uri) {
        val destinationUri = Uri.fromFile(File(cacheDir, "croppedImage.jpg"))

        val options = UCrop.Options()
        UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(16f, 9f)
            .withOptions(options.apply {
                setStatusBarColor(ContextCompat.getColor(this@MainActivity, R.color.primary))
                setToolbarColor(ContextCompat.getColor(this@MainActivity, R.color.primary))
                setToolbarTitle(ContextCompat.getString(this@MainActivity, R.string.crop))
                setToolbarWidgetColor(ContextCompat.getColor(this@MainActivity, R.color.white))
            })
            .withMaxResultSize(1080, 1080)
            .start(this)
    }

    private fun analyzeImage() {
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object: ImageClassifierHelper.ClassifierListener {
                override fun onError(message: String) {
                    lifecycleScope.launch(Dispatchers.Main) {
                        showToast(message)
                    }
                }

                override fun onResults(results: List<Classifications>?) {
                    lifecycleScope.launch(Dispatchers.Main) {
                        results?.let { result ->
                            if (result.isNotEmpty() && result[0].categories.isNotEmpty()) {
                                val topCategory = result[0].categories.maxByOrNull { it.score }
                                topCategory?.let { category ->
                                    moveToResult(currentImageUri!!, category.label, category.score)
                                }
                            } else {
                                showToast("Error analyzing image")
                            }
                        }
                    }
                }
            }
        )
        if (currentImageUri != null) {
            imageClassifierHelper.classifyStaticImage(imageUri = currentImageUri!!)
        } else {
            showToast("Choose image first!")
        }
    }

    private fun moveToResult(imageUri: Uri, result: String, score: Float) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("imageUri", imageUri.toString())
        intent.putExtra("label", result)
        intent.putExtra("score", score)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}