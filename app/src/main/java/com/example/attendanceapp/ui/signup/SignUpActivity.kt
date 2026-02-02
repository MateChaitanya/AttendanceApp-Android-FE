//package com.example.attendanceapp.ui.signup
//
//import android.os.Bundle
//import android.util.Patterns
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.attendanceapp.databinding.ActivitySignupBinding
//
//class SignUpActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivitySignupBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivitySignupBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        binding.btnSignup.setOnClickListener {
//            validateAndSignup()
//        }
//    }
//
//    private fun validateAndSignup() {
//        val name = binding.etName.text.toString().trim()
//        val email = binding.etEmail.text.toString().trim()
//        val password = binding.etPassword.text.toString().trim()
//
//        when {
//            name.isEmpty() -> {
//                binding.etName.error = "Name required"
//            }
//
//            email.isEmpty() -> {
//                binding.etEmail.error = "Email required"
//            }
//
//            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
//                binding.etEmail.error = "Invalid email"
//            }
//
//            password.isEmpty() -> {
//                binding.etPassword.error = "Password required"
//            }
//
//            password.length < 6 -> {
//                binding.etPassword.error = "Min 6 characters"
//            }
//
//            else -> {
//                Toast.makeText(this, "Signup successful", Toast.LENGTH_SHORT).show()
//                finish() // go back to login
//            }
//        }
//    }
//}


package com.example.attendanceapp.ui.signup

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.attendanceapp.databinding.ActivitySignupBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignup.setOnClickListener {
            validateAndSignup()
        }
    }

    private fun validateAndSignup() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val rePassword = binding.etRePassword.text.toString().trim()

        when {
            name.isEmpty() -> {
                binding.etName.error = "Name required"
            }

            email.isEmpty() -> {
                binding.etEmail.error = "Email required"
            }

            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.etEmail.error = "Invalid email"
            }

            password.isEmpty() -> {
                binding.etPassword.error = "Password required"
            }

            password.length < 6 -> {
                binding.etPassword.error = "Minimum 6 characters"
            }

            password != rePassword -> {
                binding.etRePassword.error = "Passwords do not match"
            }

            else -> {
                Toast.makeText(this, "Signup successful", Toast.LENGTH_SHORT).show()
                finish() // back to login
            }
        }
    }
}
