package com.example.attendanceapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.attendanceapp.api.ApiService
import com.example.attendanceapp.databinding.ActivityLoginBinding
import com.example.attendanceapp.model.LoginRequest
import com.example.attendanceapp.model.LoginResponse
import com.example.attendanceapp.network.RetrofitClient
import com.example.attendanceapp.ui.attendance.DashboardActivity
import com.example.attendanceapp.ui.signup.SignUpActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Use the singleton ApiService from RetrofitClient
        apiService = RetrofitClient.apiService

        // 🔐 LOGIN BUTTON
        binding.loginbtn.setOnClickListener {

            val email = binding.username.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this,
                    "Please enter email and password",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            loginUser(email, password)
        }

        // ➕ GO TO SIGNUP
        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    // ---------------- LOGIN API CALL ----------------
    private fun loginUser(email: String, password: String) {

        lifecycleScope.launch {

            try {
                // Make API call on IO thread
                val response = withContext(Dispatchers.IO) {
                    apiService.login(LoginRequest(email, password)).execute()
                }

                if (response.isSuccessful && response.body() != null) {

                    val loginResponse: LoginResponse = response.body()!!

                    val userId = loginResponse.userId
                    val userName = loginResponse.name

                    Toast.makeText(
                        this@LoginActivity,
                        "Welcome $userName",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Navigate to DashboardActivity
                    val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                    intent.putExtra("USER_ID", userId)
                    intent.putExtra("USER_NAME", userName)
                    startActivity(intent)
                    finish()

                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Invalid email or password",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: Exception) {
                Toast.makeText(
                    this@LoginActivity,
                    "Server error: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
