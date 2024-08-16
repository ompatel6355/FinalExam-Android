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

class DataManager private constructor(private val context: Context) {
    private val BASE_URL = "https://mdev1004-finalapi.onrender.com/"
    private val sharedPreferences = context.getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)

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

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: DataManager? = null

        fun instance(context: Context): DataManager {
            if (INSTANCE == null) {
                INSTANCE = DataManager(context.applicationContext)
            }
            return INSTANCE!!
        }
    }

    private val service: BuildingAPIService by lazy {
        retrofit.create(BuildingAPIService::class.java)
    }

    fun getAllBuildings(callback: Callback<ApiResponse<List<Building>>>) {
        service.getAllBuildings().enqueue(callback)
    }

    fun getBuildingById(id: String, callback: Callback<ApiResponse<Building>>) {
        service.getBuildingById(id).enqueue(callback)
    }

    fun addBuilding(building: Building, callback: Callback<ApiResponse<Building>>) {
        service.addBuilding(building).enqueue(callback)
    }

    fun updateBuilding(id: String, building: Building, callback: Callback<ApiResponse<Building>>) {
        service.updateBuilding(id, building).enqueue(callback)
    }

    fun deleteBuilding(id: String, callback: Callback<ApiResponse<String>>) {
        service.deleteBuilding(id).enqueue(callback)
    }

    fun registerUser(newUser: MyUser, callback: Callback<ApiResponse<MyUser>>) {
        service.registerUser(newUser).enqueue(callback)
    }

    fun loginUser(credentials: MyUser, callback: Callback<ApiResponse<MyUser>>) {
        service.loginUser(credentials).enqueue(callback)
    }
}
