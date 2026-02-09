package com.example.attendanceapp.model

data class EmployeeLocationRequest(
    val userId: Long,
    val latitude: Double,
    val longitude: Double
)
