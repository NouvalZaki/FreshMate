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

class FruitListViewModel : ViewModel() {

    private val _listFruits = MutableLiveData<List<DataFruit>>()
    val listOfFruit: LiveData<List<DataFruit>> = _listFruits

    private val _isLoading = MutableLiveData<Boolean>()
    val load: LiveData<Boolean> = _isLoading

    init {
        fetchListFruits()
    }

    private fun fetchListFruits() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response: Response<FruitListResponse> = ApiConfig.getApiService().getFruits()
                if (response.isSuccessful) {
                    _listFruits.value = response.body()?.data ?: emptyList()
                } else {
                    Log.e(TAG, "Error fetching fruits: ${response.code()} ${response.message()}")
                    _listFruits.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception while fetching fruits", e)
                _listFruits.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    companion object {
        private const val TAG = "FruitListViewModel"
    }
}
