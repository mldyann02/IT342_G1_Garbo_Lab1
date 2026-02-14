package com.cit.authapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        val name = intent.getStringExtra("user_name")
        val email = intent.getStringExtra("user_email")

        if (!name.isNullOrEmpty()) {
            tvWelcome.text = "Welcome, $name"
        } else if (!email.isNullOrEmpty()) {
            tvWelcome.text = "Welcome, $email"
        } else {
            tvWelcome.text = "Welcome!"
        }

        btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}
