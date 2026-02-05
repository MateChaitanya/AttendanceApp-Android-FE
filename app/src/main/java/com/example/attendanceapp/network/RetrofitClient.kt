package com.example.attendanceapp.network

import com.example.attendanceapp.api.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // Use your local network IP (computer IP visible to your phone)
    //private const val BASE_URL = "http://192.168.1.33:8080/"
    private const val BASE_URL = "http://10.28.91.157:8080/"


    // Retrofit instance
    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // ApiService instance
    val apiService: ApiService by lazy {
        instance.create(ApiService::class.java)
    }
}
