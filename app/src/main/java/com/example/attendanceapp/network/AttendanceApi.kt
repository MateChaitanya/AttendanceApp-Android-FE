package com.example.attendanceapp.network

import com.example.attendanceapp.model.AttendanceRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AttendanceApi {

    @POST("api/attendance")
    fun markAttendance(
        @Body request: AttendanceRequest
    ): Call<Void>
}
