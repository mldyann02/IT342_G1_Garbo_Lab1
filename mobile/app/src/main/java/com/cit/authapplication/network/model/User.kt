package com.cit.authapplication.network.model

data class User(
    val userId: Long?,
    val email: String?,
    val password: String?,
    val firstName: String?,
    val lastName: String?,
    val gender: String?
)
