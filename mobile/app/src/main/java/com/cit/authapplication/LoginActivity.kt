package com.cit.authapplication

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.cit.authapplication.network.RetrofitClient

class LoginActivity : AppCompatActivity() {
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvRegisterLink = findViewById<TextView>(R.id.tvRegisterLink)
        val tvError = findViewById<TextView>(R.id.tvError)
        val btnTogglePassword = findViewById<ImageButton>(R.id.btnTogglePassword)

        // Password visibility toggle
        btnTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                etPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                btnTogglePassword.setImageResource(R.drawable.ic_visibility)
            } else {
                etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                btnTogglePassword.setImageResource(R.drawable.ic_visibility_off)
            }
            etPassword.setSelection(etPassword.text?.length ?: 0)
        }

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Hide error message
                tvError.visibility = View.GONE
                
                // init retrofit with context so interceptor can read token
                RetrofitClient.init(applicationContext)
                val req = com.cit.authapplication.network.model.LoginRequest(email = email, password = password)
                val call = RetrofitClient.apiService.login(req)
                call.enqueue(object : retrofit2.Callback<com.cit.authapplication.network.model.AuthResponse> {
                    override fun onResponse(
                        call: retrofit2.Call<com.cit.authapplication.network.model.AuthResponse>,
                        response: retrofit2.Response<com.cit.authapplication.network.model.AuthResponse>
                    ) {
                        if (response.isSuccessful) {
                            val token = response.body()?.token
                            if (!token.isNullOrEmpty()) {
                                com.cit.authapplication.network.TokenManager.saveToken(applicationContext, token)
                            }
                            val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                            intent.putExtra("user_email", email)
                            startActivity(intent)
                            finish()
                        } else {
                            // Show error message in web-consistent style
                            tvError.text = "Authentication failed. Invalid email or password"
                            tvError.visibility = View.VISIBLE
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<com.cit.authapplication.network.model.AuthResponse>, t: Throwable) {
                        // Show error message in web-consistent style
                        tvError.text = "Network error. Please try again."
                        tvError.visibility = View.VISIBLE
                    }
                })
            } else {
                // Show validation errors
                if (email.isEmpty()) {
                    etEmail.error = "Required"
                }
                if (password.isEmpty()) {
                    etPassword.error = "Required"
                }
            }
        }

        tvRegisterLink.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
