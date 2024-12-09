package com.example.freshmate.ui.fruitDiseases

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freshmate.data.response.DataDisease
import com.example.freshmate.data.response.DiseaseResponse
import com.example.freshmate.data.response.FruitListResponse
import com.example.freshmate.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Response

class DiseasesViewModel : ViewModel() {
    private val _listDisease = MutableLiveData<List<DataDisease>>()
    val listOfDisease: LiveData<List<DataDisease>> = _listDisease

    private val _isLoading = MutableLiveData<Boolean>()
    val load: LiveData<Boolean> = _isLoading

    init {
        fetchDiseases()
    }

    private fun fetchDiseases() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response: Response<DiseaseResponse> = ApiConfig.getApiService().getDiseases()
                if (response.isSuccessful) {
                    val data = response.body()?.data?.filterNotNull()
                    _listDisease.value = data ?: emptyList()
                } else {
                    Log.e(TAG, "Error fetching diseases: ${response.code()} ${response.message()}")
                    _listDisease.value = emptyList()
                }

            } catch (e: Exception) {
                Log.e(TAG, "Exception while fetching diseases", e)
                _listDisease.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }


    companion object {
        private const val TAG = "DiseasesViewModel"
    }
}