package com.cit.authapplication.network.model

data class RegisterRequest(
    val email: String,
    val password: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val gender: String? = null
)
