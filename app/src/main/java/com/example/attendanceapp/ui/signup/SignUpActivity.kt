package com.example.attendanceapp.ui.signup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.attendanceapp.databinding.ActivitySignupBinding
import com.example.attendanceapp.model.SignUpRequest
import com.example.attendanceapp.model.SignUpResponse
import com.example.attendanceapp.network.RetrofitClient
import com.example.attendanceapp.ui.login.LoginActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Signup button click
        binding.btnSignup.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val rePassword = binding.etRePassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != rePassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            signUpUser(name, email, password)
        }

        // Optional: redirect to Login
        binding.info.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun signUpUser(name: String, email: String, password: String) {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.signup(SignUpRequest(name, email, password)).execute()
                }

                if (response.isSuccessful && response.body() != null) {
                    val signUpResponse: SignUpResponse = response.body()!!
                    Toast.makeText(this@SignUpActivity, signUpResponse.message, Toast.LENGTH_SHORT).show()
                    if (signUpResponse.success) {
                        startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                        finish()
                    }
                } else {
                    Toast.makeText(this@SignUpActivity, "Signup failed", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@SignUpActivity, "Server error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
