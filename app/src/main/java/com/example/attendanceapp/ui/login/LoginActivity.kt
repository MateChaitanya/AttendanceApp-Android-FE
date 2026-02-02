package com.example.attendanceapp.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.attendanceapp.databinding.ActivityLoginBinding
import com.example.attendanceapp.ui.attendance.DashboardActivity
import com.example.attendanceapp.ui.signup.SignUpActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Login button
        binding.loginbtn.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish() // prevents going back to login
        }

        // Signup text
        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}
