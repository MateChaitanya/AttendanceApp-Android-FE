//package com.example.attendanceapp.ui.attendance
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import com.example.attendanceapp.databinding.ActivityDashboardBinding
//
//class DashboardActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityDashboardBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityDashboardBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // Navigate to Mark Attendance page
//        binding.btnMarkAttendance.setOnClickListener {
//            startActivity(
//                Intent(this, MarkAttendanceActivity::class.java)
//            )
//        }
//    }
//}


package com.example.attendanceapp.ui.attendance

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.attendanceapp.databinding.ActivityDashboardBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.*

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setTodayDate()

        binding.btnCheckIn.setOnClickListener {
            val time = getCurrentTime()
            binding.tvCheckIn.text = "Check-In: $time"

            getCurrentLocation()   // 🔥 LOCATION SHOWN HERE

            binding.btnCheckIn.isEnabled = false
            binding.btnCheckOut.isEnabled = true
        }

        binding.btnCheckOut.setOnClickListener {
            val time = getCurrentTime()
            binding.tvCheckOut.text = "Check-Out: $time"
            binding.btnCheckOut.isEnabled = false
        }
    }

    private fun setTodayDate() {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        binding.tvDate.text = "Today: ${sdf.format(Date())}"
    }

    private fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val lat = location.latitude
                val lng = location.longitude

                // 👇 THIS LINE DISPLAYS LOCATION ON SCREEN
                binding.tvLocation.text = "Location: $lat, $lng"
            } else {
                binding.tvLocation.text = "Location: Unable to fetch"
            }
        }
    }
}
//hi
