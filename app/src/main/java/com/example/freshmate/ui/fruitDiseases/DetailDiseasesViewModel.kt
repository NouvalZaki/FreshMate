package com.example.freshmate.ui.fruitDiseases

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freshmate.data.response.DataDisease
import com.example.freshmate.data.response.DiseaseResponse
import com.example.freshmate.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Response

class DetailDiseasesViewModel: ViewModel() {
    private val apiService = ApiConfig.getApiService()
    private val _diseaseDetail = MutableLiveData<DataDisease>()
    val diseaseDetail: LiveData<DataDisease> get() = _diseaseDetail
    private val _disease = MutableLiveData<List<DataDisease>>()
    val disease: LiveData<List<DataDisease>> get() = _disease


    fun fetchDetailDisease(diseaseId: Int) {
        viewModelScope.launch {
            Log.d("DetailViewModel", "Loading started")  // Debug log
            try {
                // Fetch the list of fruits from the API
                val response: Response<DiseaseResponse> = apiService.getDiseases()
                if (response.isSuccessful) {
                    val diseaseList = response.body()?.data  // Extract the list of fruits

                    // Find the specific fruit by its ID
                    val diseaseDetail = diseaseList?.find { it.id == diseaseId }

                    // Update LiveData with the found fruit or log an error if null
                    diseaseDetail?.let {
                        _diseaseDetail.value = it
                    } ?: Log.e("DetailViewModel", "Disease with ID $diseaseId not found")
                } else {
                    Log.e("DetailViewModel", "Error fetching disease list: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Exception: ${e.message}")
            } finally {
                Log.d("DetailViewModel", "Loading finished")
            }
        }
    }



}