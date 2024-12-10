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
import com.example.freshmate.ui.home.HomeFragment
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
            if (it.ripeness == "ripe") {
                binding.tvResult.text = "Matang"
            } else if (it.ripeness == "unripe") {
                binding.tvResult.text = "Belum Matang"
            }else {
                binding.tvResult.text = "Busuk"
            }

            if (it.fruitType == "Apple") {
                binding.tvFruit.text = "Buah Apel"
            } else if (it.fruitType == "Banana") {
                binding.tvFruit.text = "Buah Pisang"
            } else if (it.fruitType == "Orange") {
                binding.tvFruit.text = "Buah Jeruk"
            } else if (it.fruitType == "Pineapple") {
                binding.tvFruit.text = "Buah Nanas"
            } else if (it.fruitType == "DragonFruit") {
                binding.tvFruit.text = "Buah Naga"
            } else if (it.fruitType == "Grape") {
                binding.tvFruit.text = "Buah Anggur"
            } else if (it.fruitType == "Guava") {
                binding.tvFruit.text = "Jambu Biji"
            } else if (it.fruitType == "Papaya") {
                binding.tvFruit.text = "Buah Pepaya"
            } else if (it.fruitType == "Pomegranate") {
                binding.tvFruit.text = "Buah Delima"
            } else if (it.fruitType == "Strawberry") {
                binding.tvFruit.text = "Buah Strawberry"
            }
                binding.circularBar.apply {
                    progress = it.confidence!!
                    setProgressWithAnimation( progress, 1000)
                    progressMax = 1f
                    if (progress >= 0.7f && progress <= 1f ) {
                        progressBarColor = Color.GREEN
                        progressBarColorDirection = CircularProgressBar.GradientDirection.RIGHT_TO_LEFT
                        progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
                        binding.tvConfident.text = "${it.confidence * 100}%"
                    }
                    else if (progress <= 0.7f && progress >= 0.2f ) {
                        progressBarColor = Color.YELLOW
                        progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
                        progressBarColorDirection = CircularProgressBar.GradientDirection.RIGHT_TO_LEFT
                        binding.tvConfident.text = "${it.confidence * 100}%"
                    }else if (progress <= 0.2f ) {
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
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            }
        }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
        finish() //
    }
}