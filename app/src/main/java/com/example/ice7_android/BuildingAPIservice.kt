package com.example.ice7_android

import retrofit2.Call
import retrofit2.http.*

interface BuildingAPIService {

    // Display the building list
    @GET("/api/")
    fun getAllBuildings(): Call<ApiResponse<List<Building>>>

    // Display a building by ID
    @GET("/api/{id}")
    fun getBuildingById(@Path("id") id: String): Call<ApiResponse<Building>>

    // Add a new building
    @POST("/api/add")
    fun addBuilding(@Body building: Building): Call<ApiResponse<Building>>

    // Update a building by ID
    @PUT("/api/update/{id}")
    fun updateBuilding(@Path("id") id: String, @Body building: Building): Call<ApiResponse<Building>>

    // Delete a building by ID
    @DELETE("api/delete/{id}")
    fun deleteBuilding(@Path("id") id: String): Call<ApiResponse<String>>

    // Register a User
    @POST("/api/register")
    fun registerUser(@Body newUser: MyUser): Call<ApiResponse<MyUser>>

    // Login a User
    @POST("/api/login")
    fun loginUser(@Body credentials: MyUser): Call<ApiResponse<MyUser>>
}
