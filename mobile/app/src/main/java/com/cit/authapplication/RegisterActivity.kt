package com.cit.authapplication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.cit.authapplication.network.RetrofitClient
import com.cit.authapplication.network.TokenManager
import com.cit.authapplication.network.model.RegisterRequest
import com.cit.authapplication.network.model.AuthResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var tvRuleLength: TextView
    private lateinit var tvRuleUpper: TextView
    private lateinit var tvRuleNumber: TextView
    private lateinit var tvRuleSpecial: TextView
    private lateinit var tvRuleMatch: TextView
    private var isPasswordVisible = false
    private var isConfirmVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etFirstName = findViewById<EditText>(R.id.etFirstName)
        val etLastName = findViewById<EditText>(R.id.etLastName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirm = findViewById<EditText>(R.id.etConfirmPassword)
        val spinnerGender = findViewById<Spinner>(R.id.spinnerGender)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvLoginLink = findViewById<TextView>(R.id.tvLoginLink)
        val tvError = findViewById<TextView>(R.id.tvError)
        
        // Validation rule text views
        tvRuleLength = findViewById(R.id.tvRuleLength)
        tvRuleUpper = findViewById(R.id.tvRuleUpper)
        tvRuleNumber = findViewById(R.id.tvRuleNumber)
        tvRuleSpecial = findViewById(R.id.tvRuleSpecial)
        tvRuleMatch = findViewById(R.id.tvRuleMatch)

        // Setup gender spinner
        val genderOptions = arrayOf("Select Identity", "Male", "Female", "Other")
        spinnerGender.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genderOptions)

        // Setup password toggle buttons
        val btnTogglePassword = findViewById<ImageButton>(R.id.btnTogglePassword)
        val btnToggleConfirmPassword = findViewById<ImageButton>(R.id.btnToggleConfirmPassword)

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

        btnToggleConfirmPassword.setOnClickListener {
            isConfirmVisible = !isConfirmVisible
            if (isConfirmVisible) {
                etConfirm.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                btnToggleConfirmPassword.setImageResource(R.drawable.ic_visibility)
            } else {
                etConfirm.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                btnToggleConfirmPassword.setImageResource(R.drawable.ic_visibility_off)
            }
            etConfirm.setSelection(etConfirm.text?.length ?: 0)
        }

        // Real-time password validation
        val passwordWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val password = s?.toString() ?: ""
                updateValidationUI(password, etConfirm.text?.toString() ?: "")
            }
        }

        val confirmWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                updateValidationUI(etPassword.text?.toString() ?: "", s?.toString() ?: "")
            }
        }

        etPassword.addTextChangedListener(passwordWatcher)
        etConfirm.addTextChangedListener(confirmWatcher)

        btnRegister.setOnClickListener {
            val firstName = etFirstName.text.toString().trim()
            val lastName = etLastName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()
            val confirm = etConfirm.text.toString()

            val genderPosition = spinnerGender.selectedItemPosition
            val gender = when (genderPosition) {
                1 -> "male"
                2 -> "female"
                3 -> "other"
                else -> ""
            }

            // Validation
            var valid = true
            if (firstName.isEmpty()) { etFirstName.error = "Required"; valid = false }
            if (lastName.isEmpty()) { etLastName.error = "Required"; valid = false }
            if (email.isEmpty()) { etEmail.error = "Required"; valid = false }
            if (password.isEmpty()) { etPassword.error = "Required"; valid = false }
            if (confirm.isEmpty()) { etConfirm.error = "Required"; valid = false }
            if (genderPosition == 0) {
                Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show()
                valid = false
            }

            val lengthOk = password.length >= 8
            val upperOk = Regex("[A-Z]").containsMatchIn(password)
            val numberOk = Regex("[0-9]").containsMatchIn(password)
            val specialOk = Regex("[!@#\\$%\\^&*(),.?\":{}|<>]").containsMatchIn(password)
            val matchOk = password == confirm && confirm.isNotEmpty()

            if (!lengthOk || !upperOk || !numberOk || !specialOk || !matchOk) {
                valid = false
            }

            if (!valid) {
                tvError.text = "Please ensure the password meets all security requirements."
                tvError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            // Hide error and proceed
            tvError.visibility = View.GONE

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
                                tvError.text = error
                                tvError.visibility = View.VISIBLE
                            }
                            else -> {
                                tvError.text = "Registration failed"
                                tvError.visibility = View.VISIBLE
                            }
                        }
                    } else {
                        if (response.code() == 409) {
                            tvError.text = "Email already exists. Cannot register."
                        } else {
                            tvError.text = "Registration failed. Check your details."
                        }
                        tvError.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    tvError.text = "Network error. Please try again."
                    tvError.visibility = View.VISIBLE
                }
            })
        }

        tvLoginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun updateValidationUI(password: String, confirmPassword: String) {
        val lengthOk = password.length >= 8
        val upperOk = Regex("[A-Z]").containsMatchIn(password)
        val numberOk = Regex("[0-9]").containsMatchIn(password)
        val specialOk = Regex("[!@#\\$%\\^&*(),.?\":{}|<>]").containsMatchIn(password)
        val matchOk = password == confirmPassword && confirmPassword.isNotEmpty()

        updateRuleColor(tvRuleLength, lengthOk)
        updateRuleColor(tvRuleUpper, upperOk)
        updateRuleColor(tvRuleNumber, numberOk)
        updateRuleColor(tvRuleSpecial, specialOk)
        updateRuleColor(tvRuleMatch, matchOk)
    }

    private fun updateRuleColor(textView: TextView, isValid: Boolean) {
        if (isValid) {
            textView.setTextColor(resources.getColor(R.color.green_600, null))
        } else {
            textView.setTextColor(resources.getColor(R.color.text_gray, null))
        }
    }
}
