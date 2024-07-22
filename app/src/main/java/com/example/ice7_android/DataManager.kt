package com.example.ice7_android

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.Callback
import retrofit2.converter.moshi.MoshiConverterFactory
import android.annotation.SuppressLint
import android.content.Context
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/* Singleton */
class DataManager private constructor(private val context: Context) {
    private val BASE_URL = "https://mdev1004-m2024-api-q9bi.onrender.com/api/"
    private val sharedPreferences = context.getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)

    // converts JSON to Data we can use
    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val builder = originalRequest.newBuilder()
                sharedPreferences.getString("auth_token", null)?.let {
                    builder.addHeader("Authorization", "Bearer $it")
                }
                val newRequest = builder.build()
                chain.proceed(newRequest)
            }
            .build()
    }

    // Retrofit enables REQ / RES with APIs
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // interceptor
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: DataManager? = null

        fun instance(context: Context): DataManager {
            if(INSTANCE == null) {
                INSTANCE = DataManager(context.applicationContext)
            }
            return INSTANCE!!
        }
    }

    private val service: MovieAPIService by lazy {
        retrofit.create(MovieAPIService::class.java)
    }

    fun getAllMovies(callback: Callback<ApiResponse<List<Movie>>>) {
        service.getAllMovies().enqueue(callback)
    }

    fun getMovieById(id: String?, callback: Callback<ApiResponse<Movie>>){
        service.getMovieById(id).enqueue(callback)
    }

    fun addMovie(movie: Movie, callback: Callback<ApiResponse<Movie>>) {
        service.addMovie(movie).enqueue(callback)
    }

    fun updateMovie(id: String?, movie: Movie, callback: Callback<ApiResponse<Movie>>) {
        service.updateMovie(id, movie).enqueue(callback)
    }

    fun deleteMovie(id: String?, callback: Callback<ApiResponse<String>>) {
        service.deleteMovie(id).enqueue(callback)
    }

    fun registerUser(newUser: MyUser, callback: Callback<ApiResponse<MyUser>>) {
        service.registerUser(newUser).enqueue(callback)
    }

    fun loginUser(credentials: MyUser, callback: Callback<ApiResponse<MyUser>>) {
        service.loginUser(credentials).enqueue(callback)
    }
}
