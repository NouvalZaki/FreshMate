package com.example.freshmate.data.retrofit

import com.example.freshmate.data.response.FruitListResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

val retrofit = Retrofit.Builder()
    .baseUrl("https://mocki.io/v1/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface ApiService {
    @GET("3eac5662-9f36-48be-9905-a836808deb49")
    suspend fun getFruits(): Response<FruitListResponse>
}