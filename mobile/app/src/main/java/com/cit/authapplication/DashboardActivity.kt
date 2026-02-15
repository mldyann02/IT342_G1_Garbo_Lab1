package com.cit.authapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.cit.authapplication.network.RetrofitClient

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        val name = intent.getStringExtra("user_name")
        val email = intent.getStringExtra("user_email")

        // Initialize Retrofit (so the interceptor can access token)
        try {
            RetrofitClient.init(applicationContext)
            RetrofitClient.apiService.me().enqueue(object : retrofit2.Callback<com.cit.authapplication.network.model.User> {
                override fun onResponse(
                    call: retrofit2.Call<com.cit.authapplication.network.model.User>,
                    response: retrofit2.Response<com.cit.authapplication.network.model.User>
                ) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        val display = when {
                            !user?.firstName.isNullOrEmpty() -> {
                                val fullName = listOfNotNull(user?.firstName, user?.lastName).joinToString(" ").trim()
                                getString(R.string.welcome_with_name, fullName)
                            }
                            !user?.email.isNullOrEmpty() -> getString(R.string.welcome_with_name, user?.email)
                            else -> getString(R.string.welcome_default)
                        }
                        tvWelcome.text = display
                    } else {
                        tvWelcome.text = getString(R.string.welcome_default)
                    }
                }

                override fun onFailure(call: retrofit2.Call<com.cit.authapplication.network.model.User>, t: Throwable) {
                    if (!name.isNullOrEmpty()) {
                        tvWelcome.text = getString(R.string.welcome_with_name, name)
                    } else if (!email.isNullOrEmpty()) {
                        tvWelcome.text = getString(R.string.welcome_with_name, email)
                    } else {
                        tvWelcome.text = getString(R.string.welcome_default)
                    }
                }
            })
        } catch (e: Exception) {
            if (!name.isNullOrEmpty()) {
                tvWelcome.text = getString(R.string.welcome_with_name, name)
            } else if (!email.isNullOrEmpty()) {
                tvWelcome.text = getString(R.string.welcome_with_name, email)
            } else {
                tvWelcome.text = getString(R.string.welcome_default)
            }
        }

        btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            com.cit.authapplication.network.TokenManager.clear(applicationContext)
            startActivity(intent)
            finish()
        }
    }
}
