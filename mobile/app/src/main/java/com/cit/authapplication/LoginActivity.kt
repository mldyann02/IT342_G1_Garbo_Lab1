package com.cit.authapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvRegisterLink = findViewById<TextView>(R.id.tvRegisterLink)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {
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
                            etPassword.error = "Login failed: ${response.code()}"
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<com.cit.authapplication.network.model.AuthResponse>, t: Throwable) {
                        etPassword.error = "Network error"
                    }
                })
            } else {
                etEmail.error = if (email.isEmpty()) "Required" else null
                etPassword.error = if (password.isEmpty()) "Required" else null
            }
        }

        tvRegisterLink.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
