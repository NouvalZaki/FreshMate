package com.example.freshmate.ui.camera

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class CameraViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    val imageUri = savedStateHandle.getLiveData<Uri>("imageUri")
}