package com.example.freshmate.ui.camera

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.OrientationEventListener
import android.view.Surface
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraProvider
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.example.freshmate.R
import com.example.freshmate.data.response.ImageUploadResponse
import com.example.freshmate.data.retrofit.ApiConfig
import com.example.freshmate.databinding.ActivityCameraBinding
import com.example.freshmate.ui.analyze.DetailAnalyzeActivity
import com.example.freshmate.util.CameraViewModelFactory
import com.example.freshmate.util.createCustomTempFile
import com.example.freshmate.util.reduceFileImage
import com.example.freshmate.util.uriToFile
import com.google.gson.Gson
import com.lottiefiles.dotlottie.core.util.lifecycleOwner
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class CameraActivity : AppCompatActivity() {

    private val binding by lazy { ActivityCameraBinding.inflate(layoutInflater) }
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null
    private val cameraViewModel by viewModels<CameraViewModel>()
    var isFlashOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        hideSystemUI()
        startCamera()

        binding.btnRetake.setOnClickListener { retakePhoto() }
        binding.captureImage.setOnClickListener { takePhoto() }
        binding.btnAnalyze.setOnClickListener { analyzeImage() }
        binding.galeryImage.setOnClickListener { startGalery() }
        binding.flash.setOnClickListener { toggleFlashlight() }
    }

    private fun toggleFlashlight() {
        isFlashOn = !isFlashOn
        val cameraProvider = ProcessCameraProvider.getInstance(this).get()
        val camera = cameraProvider.bindToLifecycle(this, cameraSelector)
        val flashMode = if (isFlashOn) ImageCapture.FLASH_MODE_ON else ImageCapture.FLASH_MODE_OFF

        imageCapture?.flashMode = flashMode

        if (camera.cameraInfo.hasFlashUnit()) {
            camera.cameraControl.enableTorch(isFlashOn)
        }

        binding.flash.setImageResource(if (isFlashOn) R.drawable.ic_flash1 else R.drawable.ic_flash0)
    }


    private fun startGalery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            UCrop.of(uri, Uri.fromFile(cacheDir.resolve("${System.currentTimeMillis()}.jpg")))
                .withAspectRatio(6F, 6F)
                .withMaxResultSize(2000, 2000)
                .start(this)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            val resultUri = UCrop.getOutput(data!!)
            cameraViewModel.imageUri.value = resultUri
            updateVisibility(true)
            Log.d("Image Uri", "onActivityResult: $resultUri")
        }else if(requestCode == UCrop.REQUEST_CROP && resultCode == UCrop.RESULT_ERROR){
            val cropError = UCrop.getError(data!!)
            Log.e("Image Uri", "onActivityResult: $cropError")
        }
    }

    private fun startCamera(){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            @Suppress("DEPRECATION")
            val preview = Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                .setTargetRotation(Surface.ROTATION_0)
                .build()
                .also {
                    it.surfaceProvider = binding.previewView.surfaceProvider
                }

            @Suppress("DEPRECATION")
            imageCapture = ImageCapture.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(Surface.ROTATION_0)
                .build()

            cameraViewModel.imageUri.observe(this) { capturedImageUri ->
                capturedImageUri?.let {
                    binding.ivImageTaken.setImageURI(it)
                    updateVisibility(true)
                }
            }

            try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        this,
                        cameraSelector,
                        preview,
                        imageCapture
                    )
            }catch (exc: Exception){
                Toast.makeText(
                    this@CameraActivity,
                    "Gagal memunculkan kamera.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e(TAG, "startCamera: ${exc.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = createCustomTempFile(application)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Toast.makeText(this@CameraActivity, "Successful Take Photo", Toast.LENGTH_SHORT).show()
                    val capturedImageFile = outputFileResults.savedUri?.let { it.path?.let { it1 ->
                        File(it1)
                    } } ?: photoFile

                    cropImageToSquare(capturedImageFile) { croppedFile ->
                        val reducedFile = croppedFile.reduceFileImage()
                        val capturedImageUri = Uri.fromFile(reducedFile)
                        cameraViewModel.imageUri.value = capturedImageUri
                        updateVisibility(true)
                        }
                    }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(this@CameraActivity, "Failed To Take Photo", Toast.LENGTH_SHORT).show()
                    finish()
                    Log.e(TAG, "onError: ${exception.message}", exception)
                }
            }
        )
    }

    private fun moveToResult(result: ImageUploadResponse, image: Uri) {
        val intent = Intent(this, DetailAnalyzeActivity::class.java).apply {
            putExtra("image_response", result)
            putExtra("image", image)
        }
        startActivity(intent)
    }

    private fun analyzeImage(){
        cameraViewModel.imageUri.value?.let {
            val imageFile = uriToFile(it, this)
            Log.d("Image File", "showImage: ${imageFile.path}")
            showLoading(true)

            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "image",
                imageFile.name,
                requestImageFile
            )
            lifecycleScope.launch {
                try {
                    val apiService = ApiConfig.getApiService()
                    val successResponse = apiService.imageAnalyseUpload(multipartBody)
                    val response = ImageUploadResponse(
                        successResponse.fruitType,
                        successResponse.ripeness,
                        successResponse.confidence,
                        successResponse.message,
                        successResponse.timestamp
                    )
                    showLoading(false)
                    if (response.message == "Gambar kurang jelas, hasil prediksi mungkin tidak akurat") {
                        showToast("Gambar kurang jelas, hasil prediksi mungkin tidak akurat")
                        Log.d("Failed", "Failed")
                    } else{
                        this@CameraActivity.runOnUiThread { moveToResult(successResponse, it) }
                        showToast("Image Successfully Uploaded")
                        Log.d("Success", "Success")
                    }
                } catch (e: HttpException) {
                    showToast("Image Failed to Upload ${e}")
                    updateVisibility(false)
                    showLoading(false)
                    Log.e("Error", "Error")
                }
            }
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun updateVisibility(isImageCaptured: Boolean) {
        binding.apply {
            ivImageTaken.visibility = if (isImageCaptured) View.VISIBLE else View.GONE
            btnAnalyze.visibility = if (isImageCaptured) View.VISIBLE else View.GONE
            btnRetake.visibility = if (isImageCaptured) View.VISIBLE else View.GONE
            container1.visibility = if (isImageCaptured) View.VISIBLE else View.GONE
            container2.visibility = if (isImageCaptured) View.VISIBLE else View.GONE
            container0.visibility = if (isImageCaptured) View.GONE else View.VISIBLE
            squareVector.visibility = if (isImageCaptured) View.GONE else View.VISIBLE
            captureImage.visibility = if (isImageCaptured) View.GONE else View.VISIBLE
            previewView.visibility = if (isImageCaptured) View.GONE else View.VISIBLE
            galeryImage.visibility = if (isImageCaptured) View.GONE else View.VISIBLE
            flash.visibility = if (isImageCaptured) View.GONE else View.VISIBLE
        }
    }

    private fun retakePhoto() {
        cameraViewModel.imageUri.value = null
        updateVisibility(false)
        startCamera()
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun cropImageToSquare(photoFile: File, callback: (File) -> Unit) {
        val bitmap = BitmapFactory.decodeFile(photoFile.path)
        val dimension = Math.min(bitmap.width, bitmap.height)
        val x = (bitmap.width - dimension) / 1
        val y = (bitmap.height - dimension) / 2

        val croppedBitmap = Bitmap.createBitmap(bitmap, x, y, dimension, dimension)

        val croppedFile = createCustomTempFile(application)
        val outStream = FileOutputStream(croppedFile)
        croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
        outStream.close()

        callback(croppedFile)
    }

    private val orientationEventListener by lazy {
        object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN) {
                    return
                }
                val rotation = when (orientation) {
                    in 225 until 315 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }

                imageCapture?.targetRotation = rotation
                binding.captureImage.rotation = rotation.toFloat()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        orientationEventListener.enable()
    }

    override fun onStop() {
        super.onStop()
        orientationEventListener.disable()
    }

    companion object {
        private const val TAG = "CameraActivity"
    }
}