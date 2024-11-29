package com.example.freshmate.data.helper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freshmate.data.response.DataFruit
import com.example.freshmate.data.response.FruitListResponse
import com.example.freshmate.data.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _listFruits = MutableLiveData<List<DataFruit>>()
    val listOfFruit: LiveData<List<DataFruit>> = _listFruits

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "MainViewModel"
    }

    init {
        findFruits()
    }

    fun findFruits(active: Int = 0) {
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {

                val response: Response<FruitListResponse> =
                    ApiConfig.getApiService().getFruits()


                withContext(Dispatchers.Main) {
                    _isLoading.value = false

                    if (response.isSuccessful) {

                        _listFruits.value = response.body()?.data ?: listOf()
                    } else {

                        _listFruits.value = listOf()
                    }
                }
            } catch (e: Exception) {

                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                    _listFruits.value = listOf()
                }
            }

        }
    }
}
