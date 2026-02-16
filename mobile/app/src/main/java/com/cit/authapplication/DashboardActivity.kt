package com.cit.authapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.cit.authapplication.network.RetrofitClient
import com.cit.authapplication.network.model.User

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Profile UI elements
        val tvInitials = findViewById<TextView>(R.id.tvInitials)
        val tvFullName = findViewById<TextView>(R.id.tvFullName)
        val tvEmail = findViewById<TextView>(R.id.tvEmail)
        val tvGender = findViewById<TextView>(R.id.tvGender)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        // Initialize Retrofit (so the interceptor can access token)
        try {
            RetrofitClient.init(applicationContext)
            RetrofitClient.apiService.me().enqueue(object : retrofit2.Callback<User> {
                override fun onResponse(
                    call: retrofit2.Call<User>,
                    response: retrofit2.Response<User>
                ) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        updateUserProfile(user)
                    } else {
                        // Show fallback from intent extras
                        loadFromIntentExtras()
                    }
                }

                override fun onFailure(call: retrofit2.Call<User>, t: Throwable) {
                    // Show fallback from intent extras
                    loadFromIntentExtras()
                }
            })
        } catch (e: Exception) {
            // Show fallback from intent extras
            loadFromIntentExtras()
        }

        btnLogout.setOnClickListener {
            // Show logout confirmation dialog
            showLogoutConfirmationDialog()
        }
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmation")
        builder.setMessage("Are you sure you want to logout?")
        
        // Yes button - proceed with logout
        builder.setPositiveButton("Yes") { dialog, which ->
            // Clear token
            com.cit.authapplication.network.TokenManager.clear(applicationContext)
            
            // Show logout confirmation toast
            Toast.makeText(this, "You have been logged out successfully", Toast.LENGTH_SHORT).show()
            
            // Navigate to login screen
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
        
        // No button - cancel and dismiss dialog
        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }
        
        val dialog = builder.create()
        dialog.show()
    }

    private fun updateUserProfile(user: User?) {
        val tvInitials = findViewById<TextView>(R.id.tvInitials)
        val tvFullName = findViewById<TextView>(R.id.tvFullName)
        val tvEmail = findViewById<TextView>(R.id.tvEmail)
        val tvGender = findViewById<TextView>(R.id.tvGender)

        // Set initials from first letter of firstName and lastName
        val firstName = user?.firstName ?: ""
        val lastName = user?.lastName ?: ""
        val initials = when {
            firstName.isNotEmpty() && lastName.isNotEmpty() -> "${firstName[0]}${lastName[0]}".uppercase()
            firstName.isNotEmpty() -> firstName[0].uppercase()
            else -> "?"
        }
        tvInitials.text = initials

        // Set full name
        val fullName = listOfNotNull(firstName, lastName).joinToString(" ").trim()
        tvFullName.text = if (fullName.isNotEmpty()) fullName else "User"

        // Set email
        tvEmail.text = user?.email ?: "Not available"

        // Set gender with fallback
        val gender = user?.gender
        tvGender.text = if (!gender.isNullOrEmpty()) {
            gender.replaceFirstChar { it.uppercase() }
        } else {
            "Not Specified"
        }
    }

    private fun loadFromIntentExtras() {
        val name = intent.getStringExtra("user_name")
        val email = intent.getStringExtra("user_email")

        val tvFullName = findViewById<TextView>(R.id.tvFullName)
        val tvEmail = findViewById<TextView>(R.id.tvEmail)
        val tvInitials = findViewById<TextView>(R.id.tvInitials)
        val tvGender = findViewById<TextView>(R.id.tvGender)

        if (!name.isNullOrEmpty()) {
            tvFullName.text = name
            val parts = name.split(" ")
            val initials = when {
                parts.size >= 2 -> "${parts[0].firstOrNull() ?: ""}${parts[1].firstOrNull() ?: ""}".uppercase()
                parts.isNotEmpty() -> parts[0].firstOrNull()?.uppercase() ?: "?"
                else -> "?"
            }
            tvInitials.text = initials
        } else {
            tvFullName.text = "User"
            tvInitials.text = "?"
        }

        if (!email.isNullOrEmpty()) {
            tvEmail.text = email
        } else {
            tvEmail.text = "Not available"
        }

        tvGender.text = "Not Specified"
    }
}
