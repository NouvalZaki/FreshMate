package com.example.freshmate.ui.analyze

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.freshmate.R
import com.example.freshmate.data.response.ImageUploadResponse
import com.example.freshmate.databinding.ActivityDetailAnalyzeBinding
import com.example.freshmate.ui.MainActivity
import com.example.freshmate.ui.camera.CameraActivity
import com.example.freshmate.util.getBitmap
import com.google.gson.Gson
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException

class DetailAnalyzeActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDetailAnalyzeBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val analyzeResponse = intent.getParcelableExtra<ImageUploadResponse>("image_response")
        val imageUri = intent.getParcelableExtra<Uri>("image")


        analyzeResponse?.let {
            binding.tvResult.text = it.ripeness
            binding.tvFruit.text = it.fruitType
                binding.circularBar.apply {
                    progress = it.confidence!!
                    setProgressWithAnimation( it.confidence.toFloat(), 10000, null,5000)
                    progressMax = 1f
                    if (it.confidence.toInt() == 1) {
                        progressBarColor = Color.GREEN
                        progressBarColorDirection = CircularProgressBar.GradientDirection.RIGHT_TO_LEFT
                        progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
                        binding.tvConfident.text = "100%"
                    }
                    else if (it.confidence <= 0.7f && it.confidence >= 0.2f ) {
                        progressBarColor = Color.YELLOW
                        progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
                        progressBarColorDirection = CircularProgressBar.GradientDirection.RIGHT_TO_LEFT
                        binding.tvConfident.text = "${it.confidence * 100}%"
                    }else if (it.confidence <= 0.2f ) {
                        progressBarColor = Color.RED
                        progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
                        progressBarColorDirection = CircularProgressBar.GradientDirection.RIGHT_TO_LEFT
                        binding.tvConfident.text = "${it.confidence * 100}%"
                    }
                }

            }

        imageUri?.let {
            binding.imageResult.setImageURI(it)
        }

        binding.btnReanalyze.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            }
        }
    }