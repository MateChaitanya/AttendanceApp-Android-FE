package com.example.attendanceapp.ui.attendance

import android.Manifest
import android.content.pm.PackageManager
import android.os.*
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.attendanceapp.databinding.ActivityDashboardBinding
import com.example.attendanceapp.model.AttendanceRequest
import com.example.attendanceapp.network.RetrofitClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var locationClient: FusedLocationProviderClient
    private val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    private var isCheckedIn = false
    private var userId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getLongExtra("USER_ID", -1)
        binding.tvUserName.text = intent.getStringExtra("USER_NAME") ?: "User"

        locationClient = LocationServices.getFusedLocationProviderClient(this)

        startClock()
        restoreState()
        updateButtons()

        binding.btnCheckIn.setOnClickListener { handleCheckIn() }
        binding.btnCheckOut.setOnClickListener { handleCheckOut() }
    }

    // ================= CLOCK =================
    private fun startClock() {
        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                val now = Date()
                binding.tvCurrentTime.text = timeFormat.format(now)
                binding.tvDate.text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(now)
                handler.postDelayed(this, 1000)
            }
        })
    }

    // ================= CHECK-IN =================
    private fun handleCheckIn() {
        getLocation { lat, lon ->
            lifecycleScope.launch {
                try {
                    val response = withContext(Dispatchers.IO) {
                        RetrofitClient.apiService.checkIn(
                            AttendanceRequest(userId, lat, lon)
                        ).execute()
                    }

                    if (response.isSuccessful) {
                        val time = timeFormat.format(Date())
                        binding.tvCheckInTime.text = "Check-in: $time"
                        binding.tvCheckInTime.visibility = View.VISIBLE
                        binding.tvCheckOutTime.visibility = View.GONE
                        binding.tvLocation.text = "Lat: $lat, Lon: $lon"
                        binding.tvLocation.visibility = View.VISIBLE

                        isCheckedIn = true
                        saveState(time)
                        updateButtons()
                        Toast.makeText(this@DashboardActivity, "Checked In", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@DashboardActivity, "Already Checked In", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this@DashboardActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // ================= CHECK-OUT =================
    private fun handleCheckOut() {
        getLocation { lat, lon ->
            lifecycleScope.launch {
                try {
                    val response = withContext(Dispatchers.IO) {
                        RetrofitClient.apiService.checkOut(
                            AttendanceRequest(userId, lat, lon)
                        ).execute()
                    }

                    if (response.isSuccessful) {
                        val time = timeFormat.format(Date())
                        binding.tvCheckOutTime.text = "Check-out: $time"
                        binding.tvCheckOutTime.visibility = View.VISIBLE
                        binding.tvCheckInTime.visibility = View.VISIBLE
                        binding.tvLocation.text = "Lat: $lat, Lon: $lon"
                        binding.tvLocation.visibility = View.VISIBLE

                        isCheckedIn = false
                        clearState()
                        updateButtons()
                        Toast.makeText(this@DashboardActivity, "Checked Out", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@DashboardActivity, "No active check-in found", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this@DashboardActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // ================= UPDATE BUTTONS =================
    private fun updateButtons() {
        binding.btnCheckIn.isEnabled = !isCheckedIn
        binding.btnCheckOut.isEnabled = isCheckedIn
    }

    // ================= SHARED PREFERENCES =================
    private fun saveState(checkInTime: String) {
        getSharedPreferences("attendance", MODE_PRIVATE)
            .edit()
            .putBoolean("CHECKED_IN", true)
            .putString("CHECKIN_TIME", checkInTime)
            .apply()
    }

    private fun clearState() {
        getSharedPreferences("attendance", MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }

    private fun restoreState() {
        val prefs = getSharedPreferences("attendance", MODE_PRIVATE)
        isCheckedIn = prefs.getBoolean("CHECKED_IN", false)

        if (isCheckedIn) {
            val time = prefs.getString("CHECKIN_TIME", "")
            binding.tvCheckInTime.text = "Check-in: $time"
            binding.tvCheckInTime.visibility = View.VISIBLE
            binding.tvCheckOutTime.visibility = View.GONE
        } else {
            binding.tvCheckInTime.visibility = View.GONE
            binding.tvCheckOutTime.visibility = View.GONE
            binding.tvLocation.visibility = View.GONE
        }
    }

    // ================= GET LOCATION =================
    private fun getLocation(cb: (Double, Double) -> Unit) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }

        locationClient.lastLocation.addOnSuccessListener { loc ->
            loc?.let {
                cb(it.latitude, it.longitude)
            } ?: run {
                Toast.makeText(this, "Unable to fetch location", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
