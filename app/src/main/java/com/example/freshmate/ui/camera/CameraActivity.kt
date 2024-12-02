package com.example.freshmate.ui.camera

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.OrientationEventListener
import android.view.Surface
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.SavedStateHandle
import com.example.freshmate.R
import com.example.freshmate.databinding.ActivityCameraBinding
import com.example.freshmate.ui.analyze.DetailAnalyzeActivity
import com.example.freshmate.util.CameraViewModelFactory
import com.example.freshmate.util.createCustomTempFile
import com.example.freshmate.util.reduceFileImage
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date

class CameraActivity : AppCompatActivity() {

    private val binding by lazy { ActivityCameraBinding.inflate(layoutInflater) }
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraProvider: ProcessCameraProvider
    private val cameraViewModel by viewModels<CameraViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        hideSystemUI()
        startCamera()

        binding.btnRetake.setOnClickListener { retakePhoto() }
        binding.captureImage.setOnClickListener { takePhoto() }
    }
//    override fun onResume() {
//        super.onResume()
//        hideSystemUI()
//        startCamera()
//    }
    private fun startCamera(){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            @Suppress("DEPRECATION")
            val preview = Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
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
//                cameraProvider.unbindAll()
//                cameraProvider.bindToLifecycle(
//                    this,cameraSelector,preview,imageCapture
//                )
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
                        File(
                            it1
                        )
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

    private fun moveToResult(image: Uri, result: String) {
        val intent = Intent(this, DetailAnalyzeActivity::class.java)
        intent.putExtra("image", image)
        intent.putExtra("result", result)
        startActivity(intent)
    }

    private fun analyzeImage(){

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
        }
    }

    private fun retakePhoto() {
        cameraViewModel.imageUri.value = null
        updateVisibility(false)
        startCamera()
    }

    fun BitMapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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