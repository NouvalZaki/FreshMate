package com.example.freshmate.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freshmate.data.response.DataDisease
import com.example.freshmate.data.response.DataFruit
import com.example.freshmate.data.response.DiseaseResponse
import com.example.freshmate.data.response.FruitListResponse
import com.example.freshmate.data.retrofit.ApiConfig
import com.example.freshmate.ui.fruitDiseases.DiseasesViewModel
import com.example.freshmate.ui.fruitDiseases.DiseasesViewModel.Companion
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _listFruits = MutableLiveData<List<DataFruit>>()
    val listOfFruit: LiveData<List<DataFruit>> = _listFruits

    private val _listDisease = MutableLiveData<List<DataDisease>>()
    val listOfDisease: LiveData<List<DataDisease>> = _listDisease

    private val _isLoading = MutableLiveData<Boolean>()
    val load: LiveData<Boolean> = _isLoading

    init {
        fetchDetailFruit()
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
                    Log.e(HomeViewModel.TAGDISEASE, "Error fetching diseases: ${response.code()} ${response.message()}")
                    _listDisease.value = emptyList()
                }

            } catch (e: Exception) {
                Log.e(HomeViewModel.TAGDISEASE, "Exception while fetching diseases", e)
                _listDisease.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchDetailFruit() {
        viewModelScope.launch {
            _isLoading.value = true
            viewModelScope.launch {
                try {
                    val response: Response<FruitListResponse> =
                        ApiConfig.getApiService().getFruits()
                    if (response.isSuccessful) {
                        _listFruits.value = response.body()?.data ?: emptyList()
                    } else {
                        Log.e(
                            TAGFRUIT,
                            "Error fetching fruits: ${response.code()} ${response.message()}"
                        )
                        _listFruits.value = emptyList()
                    }
                } catch (e: Exception) {
                    Log.e(TAGFRUIT, "Exception while fetching fruits", e)
                    _listFruits.value = emptyList()
                } finally {
                    _isLoading.value = false
                }
            }
        }


    }

    companion object {
        private const val TAGFRUIT = "FruitListViewModel"
        private const val TAGDISEASE = "DiseasesViewModel"
    }
}