package com.example.attendanceapp.model

data class AttendanceRequest(
    val userId: Long,
    val latitude: Double,
    val longitude: Double
)
