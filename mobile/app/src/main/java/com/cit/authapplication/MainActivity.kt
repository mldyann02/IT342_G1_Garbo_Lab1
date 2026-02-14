package com.cit.authapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cit.authapplication.ui.theme.AuthApplicationTheme
import com.cit.authapplication.network.RetrofitClient
import com.cit.authapplication.network.model.LoginRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AuthApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android")
                }
            }
        }

        // Simple backend connectivity test: attempt login with placeholder credentials.
        // Backend running on host should be reachable from emulator via 10.0.2.2:8080
        val loginReq = LoginRequest(email = "test@example.com", password = "password")
        val call = RetrofitClient.apiService.login(loginReq)
        call.enqueue(object : Callback<com.cit.authapplication.network.model.AuthResponse> {
            override fun onResponse(
                call: Call<com.cit.authapplication.network.model.AuthResponse>,
                response: Response<com.cit.authapplication.network.model.AuthResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("MainActivity", "Login success, token=${body?.token}")
                } else {
                    Log.w("MainActivity", "Login failed: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<com.cit.authapplication.network.model.AuthResponse>, t: Throwable) {
                Log.e("MainActivity", "Network error", t)
            }
        })
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AuthApplicationTheme {
        Greeting("Android")
    }
}