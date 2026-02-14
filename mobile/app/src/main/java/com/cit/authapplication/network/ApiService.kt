package com.cit.authapplication.network

import com.cit.authapplication.network.model.AuthResponse
import com.cit.authapplication.network.model.LoginRequest
import com.cit.authapplication.network.model.RegisterRequest
import com.cit.authapplication.network.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("/api/auth/login")
    fun login(@Body req: LoginRequest): Call<AuthResponse>

    @POST("/api/auth/register")
    fun register(@Body req: RegisterRequest): Call<AuthResponse>

    @GET("/api/user/me")
    fun me(): Call<User>
}
