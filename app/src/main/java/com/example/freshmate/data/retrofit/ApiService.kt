package com.example.freshmate.data.retrofit

import android.media.Image
import com.example.freshmate.data.response.DiseaseResponse
import com.example.freshmate.data.response.FruitListResponse
import com.example.freshmate.data.response.ImageUploadResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @Headers("")
    @Multipart
    @POST("api/detect-ripeness")
    suspend fun imageAnalyseUpload(
        @Part image: MultipartBody.Part
    ): ImageUploadResponse

    @Headers("")
    @GET("/api/supported-fruits")
    suspend fun getFruits(): Response<FruitListResponse>

    @Headers("")
    @GET("/api/diseases-fruit")
    suspend fun getDiseases(): Response<DiseaseResponse>
}