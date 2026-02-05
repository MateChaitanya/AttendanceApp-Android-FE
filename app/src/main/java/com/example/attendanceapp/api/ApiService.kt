package com.example.attendanceapp.api

import com.example.attendanceapp.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("auth/signup")
    fun signup(@Body request: SignUpRequest): Call<SignUpResponse>

    @POST("api/attendance/check-in")
    fun checkIn(@Body request: AttendanceRequest): Call<ApiResponse>

    @POST("api/attendance/check-out")
    fun checkOut(@Body request: AttendanceRequest): Call<ApiResponse>
}

