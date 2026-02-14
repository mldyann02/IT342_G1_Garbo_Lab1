package com.cit.authapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etName = findViewById<EditText>(R.id.etName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvLoginLink = findViewById<TextView>(R.id.tvLoginLink)

        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                RetrofitClient.init(applicationContext)
                val req = com.cit.authapplication.network.model.RegisterRequest(
                    email = email,
                    password = password,
                    firstName = name
                )
                val call = RetrofitClient.apiService.register(req)
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
                            val intent = Intent(this@RegisterActivity, DashboardActivity::class.java)
                            intent.putExtra("user_name", name)
                            intent.putExtra("user_email", email)
                            startActivity(intent)
                            finish()
                        } else {
                            if (email.isEmpty()) etEmail.error = "Register failed: ${response.code()}"
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<com.cit.authapplication.network.model.AuthResponse>, t: Throwable) {
                        etEmail.error = "Network error"
                    }
                })
            } else {
                if (name.isEmpty()) etName.error = "Required"
                if (email.isEmpty()) etEmail.error = "Required"
                if (password.isEmpty()) etPassword.error = "Required"
            }
        }

        tvLoginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
