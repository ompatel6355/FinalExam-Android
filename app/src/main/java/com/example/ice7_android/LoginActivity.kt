package com.example.ice7_android

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import retrofit2.*
import com.example.ice7_android.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)

        binding.registerButton.setOnClickListener {
            // Navigate to the RegisterActivity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            loginUser(username, password)
        }
    }

//    private fun loginUser(username: String, password: String) {
//        val user = MyUser(
//            username = username,
//            emailAddress = "", // Provide default or empty value if not used
//            firstName = "", // Provide default or empty value if not used
//            lastName = "", // Provide default or empty value if not used
//            password = password
//        )
//        DataManager.instance(this).loginUser(user, object : Callback<ApiResponse<MyUser>> {
//            override fun onResponse(call: Call<ApiResponse<MyUser>>, response: Response<ApiResponse<MyUser>>) {
//                if (response.isSuccessful && response.body()?.success == true) {
//                    println("User Logged In Successfully")
//                    val token = response.body()?.token
//
//                    token?.let {
//                        val editor = sharedPreferences.edit()
//                        editor.putString("auth_token", it)
//                        editor.apply()
//
//                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
//                        finish()
//                    }
//                } else {
//                    println("User Not Logged In")
//                    showLoginFailedSnackbar("Login failed: ${response.body()?.message}")
//                }
//            }
//
//            override fun onFailure(call: Call<ApiResponse<MyUser>>, t: Throwable) {
//                println("Login Error")
//                showLoginFailedSnackbar("Login error: ${t.message}")
//            }
//        })
//    }
    private fun loginUser(username: String, password: String) {
        // Skip the actual login process
        val token = "default_auth_token" // You can set a default token or handle it as needed

        // Save the token in SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putString("auth_token", token)
        editor.apply()

        // Navigate to MainActivity
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }

    private fun showLoginFailedSnackbar(message: String) {
        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
        val view = snackbar.view
        val params = view.layoutParams as FrameLayout.LayoutParams

        params.gravity = Gravity.TOP
        view.layoutParams = params
        view.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_light))

        snackbar.show()
    }
}
