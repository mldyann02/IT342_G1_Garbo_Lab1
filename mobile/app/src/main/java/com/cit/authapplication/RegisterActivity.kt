package com.cit.authapplication

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.cit.authapplication.network.RetrofitClient
import com.cit.authapplication.network.TokenManager
import com.cit.authapplication.network.model.RegisterRequest
import com.cit.authapplication.network.model.AuthResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etFirstName = findViewById<EditText>(R.id.etFirstName)
        val etLastName = findViewById<EditText>(R.id.etLastName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirm = findViewById<EditText>(R.id.etConfirmPassword)
        val rgGender = findViewById<RadioGroup>(R.id.rgGender)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvLoginLink = findViewById<TextView>(R.id.tvLoginLink)

        // Setup password visibility toggle
        fun setupToggle(editText: EditText) {
            val eyeResId = resources.getIdentifier("design_password_eye", "drawable", packageName)
            val eyeDrawable = if (eyeResId != 0) ContextCompat.getDrawable(this, eyeResId) else null
            eyeDrawable?.let { d ->
                editText.setCompoundDrawablesWithIntrinsicBounds(null, null, d, null)
                editText.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_UP) {
                        val drawableEnd = 2
                        val drawable = editText.compoundDrawables[drawableEnd]
                        if (drawable != null) {
                            val touchX = event.x.toInt()
                            if (touchX >= editText.width - editText.paddingEnd - drawable.intrinsicWidth) {
                                val isVisible = (editText.inputType and InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) ==
                                        InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                                editText.inputType = if (isVisible)
                                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                                else
                                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                                editText.setSelection(editText.text.length)
                                return@setOnTouchListener true
                            }
                        }
                    }
                    false
                }
            }
        }

        setupToggle(etPassword)
        setupToggle(etConfirm)

        btnRegister.setOnClickListener {
            val firstName = etFirstName.text.toString().trim()
            val lastName = etLastName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()
            val confirm = etConfirm.text.toString()

            val gender = when (rgGender.checkedRadioButtonId) {
                R.id.rbMale -> "male"
                R.id.rbFemale -> "female"
                R.id.rbOther -> "other"
                else -> ""
            }

            // Validation
            var valid = true
            if (firstName.isEmpty()) { etFirstName.error = "Required"; valid = false }
            if (lastName.isEmpty()) { etLastName.error = "Required"; valid = false }
            if (email.isEmpty()) { etEmail.error = "Required"; valid = false }
            if (password.isEmpty()) { etPassword.error = "Required"; valid = false }
            if (confirm.isEmpty()) { etConfirm.error = "Required"; valid = false }

            val lengthOk = password.length >= 8
            val upperOk = Regex("[A-Z]").containsMatchIn(password)
            val numberOk = Regex("[0-9]").containsMatchIn(password)
            val specialOk = Regex("[!@#\\$%\\^&*(),.?\":{}|<>]").containsMatchIn(password)
            val matchOk = password == confirm && confirm.isNotEmpty()

            if (!lengthOk) { etPassword.error = "Requires 8+ characters"; valid = false }
            if (!upperOk) { etPassword.error = "Requires uppercase letter"; valid = false }
            if (!numberOk) { etPassword.error = "Requires number"; valid = false }
            if (!specialOk) { etPassword.error = "Requires special character"; valid = false }
            if (!matchOk) { etConfirm.error = "Passwords do not match"; valid = false }

            if (!valid) return@setOnClickListener

            // Clear existing token
            TokenManager.clear(applicationContext)

            // Init Retrofit
            RetrofitClient.init(applicationContext)
            val req = RegisterRequest(email, password, firstName, lastName, if (gender.isNotEmpty()) gender else null)
            RetrofitClient.apiService.register(req).enqueue(object : Callback<AuthResponse> {
                override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                        val token = body.token
                        val error = body.error
                        when {
                            !token.isNullOrEmpty() -> {
                                TokenManager.saveToken(applicationContext, token)
                                val intent = Intent(this@RegisterActivity, DashboardActivity::class.java)
                                intent.putExtra("user_name", "$firstName $lastName")
                                intent.putExtra("user_email", email)
                                startActivity(intent)
                                finish()
                            }
                            !error.isNullOrEmpty() -> {
                                Toast.makeText(this@RegisterActivity, "Registration failed: $error", Toast.LENGTH_LONG).show()
                            }
                            else -> {
                                Toast.makeText(this@RegisterActivity, "Unknown registration error", Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        Toast.makeText(this@RegisterActivity, "Registration failed: ${response.code()}", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    Toast.makeText(this@RegisterActivity, "Network error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            })
        }

        tvLoginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
