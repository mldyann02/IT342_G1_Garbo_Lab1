package com.cit.authapplication.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {

    // Public endpoints that don't require authentication
    private val publicEndpoints = listOf(
        "/api/auth/login",
        "/api/auth/register",
        "/api/auth/logout"
    )

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath

        // Skip adding Authorization header for public endpoints
        val isPublicEndpoint = publicEndpoints.any { path.trimEnd('/') == it }

        // Only add token if it's not a public endpoint
        if (!isPublicEndpoint) {
            val token = TokenManager.getToken(context)
            val reqBuilder = request.newBuilder()
            if (!token.isNullOrEmpty()) {
                reqBuilder.addHeader("Authorization", "Bearer $token")
            }
            return chain.proceed(reqBuilder.build())
        }

        return chain.proceed(request)
    }
}
