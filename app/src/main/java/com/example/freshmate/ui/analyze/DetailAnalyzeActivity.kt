package com.example.freshmate.ui.analyze

import android.content.Intent
import android.graphics.Bitmap
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
import com.example.freshmate.databinding.ActivityDetailAnalyzeBinding
import com.example.freshmate.ui.camera.CameraActivity
import com.example.freshmate.util.getBitmap
import com.example.freshmate.util.uriToFile
import com.google.gson.Gson
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
        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        val result = intent.getStringExtra(EXTRA_RESULT)

        imageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.imageResult.setImageURI(it)
        }

        result?.let {
            Log.d("Result", "showResult: $it")
            binding.tvResult.text = it
        }
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
    }
}