package com.example.freshmate.ui.camera

import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.viewbinding.ViewBindings
import com.example.freshmate.R
import com.example.freshmate.databinding.ActivityCameraBinding
import kotlinx.coroutines.launch

class CameraViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    val imageUri = savedStateHandle.getLiveData<Uri>("imageUri")

    fun setImageUri(uri: Uri) {
        savedStateHandle.set("imageUri", uri)
    }
}