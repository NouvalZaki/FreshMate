package com.example.freshmate.ui.fruitList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freshmate.data.response.DataFruit
import com.example.freshmate.data.response.FruitListResponse
import com.example.freshmate.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Response

class DetailActivityListViewModel : ViewModel() {
    private val apiService = ApiConfig.getApiService()
    private val _fruitDetail = MutableLiveData<DataFruit>()
    val fruitListDetail: LiveData<DataFruit> get() = _fruitDetail
    private val _fruit = MutableLiveData<List<DataFruit>>()
    val fruit: LiveData<List<DataFruit>> get() = _fruit


    fun fetchDetailFruit(fruitId: Int) {
        viewModelScope.launch {
            Log.d("DetailViewModel", "Loading started")  // Debug log
            try {
                // Fetch the list of fruits from the API
                val response: Response<FruitListResponse> = apiService.getFruits()
                if (response.isSuccessful) {
                    val fruitList = response.body()?.data  // Extract the list of fruits

                    // Find the specific fruit by its ID
                    val fruitDetail = fruitList?.find { it.id == fruitId }

                    // Update LiveData with the found fruit or log an error if null
                    fruitDetail?.let {
                        _fruitDetail.value = it
                    } ?: Log.e("DetailViewModel", "Fruit with ID $fruitId not found")
                } else {
                    Log.e("DetailViewModel", "Error fetching fruit list: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Exception: ${e.message}")
            } finally {
                Log.d("DetailViewModel", "Loading finished")
            }
        }
    }



}