//package com.example.attendanceapp.ui.attendance
//
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import com.example.attendanceapp.databinding.ActivityMarkAttendanceBinding
//import java.text.SimpleDateFormat
//import java.util.Date
//import java.util.Locale
//
//class MarkAttendanceActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityMarkAttendanceBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMarkAttendanceBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        binding.btnSubmit.setOnClickListener {
//            val time = SimpleDateFormat(
//                "dd-MM-yyyy HH:mm:ss",
//                Locale.getDefault()
//            ).format(Date())
//
//            binding.tvTime.text = "Attendance marked at:\n$time"
//        }
//    }
//}



//Updated code with permission of location

package com.example.attendanceapp.ui.attendance

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.attendanceapp.databinding.ActivityMarkAttendanceBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.*

class MarkAttendanceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMarkAttendanceBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarkAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Request location permission
        requestLocationPermission()

        binding.btnSubmit.setOnClickListener {
            markAttendance()
        }
    }

    // Step 3.1: Permission Handling
    private fun requestLocationPermission() {
        val locationPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Location permission required", Toast.LENGTH_LONG).show()
                }
            }

        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Already granted
            }
            else -> {
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    // Step 3.2: Get location + time
    @Suppress("MissingPermission")
    private fun markAttendance() {
        // Check permission
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show()
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val lat = location.latitude
                val lon = location.longitude
                val time = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(Date())

                binding.tvTime.text = "Attendance marked at:\n$time\nLat: $lat\nLon: $lon"

                // TODO: Later we will send (name, time, lat, lon) to MySQL via API
            } else {
                Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
