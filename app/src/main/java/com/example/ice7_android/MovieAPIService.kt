package com.example.ice7_android

import retrofit2.Call
import retrofit2.http.*

interface MovieAPIService
{
    // Display the movie list
    @GET("movie/list")
    fun getAllMovies(): Call<ApiResponse<List<Movie>>>

    // Display a movie by ID
    @GET("movie/find/{id}")
    fun getMovieById(@Path("id") id: String?): Call<ApiResponse<Movie>>

    // Add a new movie
    @POST("movie/add")
    fun addMovie(@Body movie: Movie): Call<ApiResponse<Movie>>

    // Update a movie by ID
    @PUT("movie/update/{id}")
    fun updateMovie(@Path("id") id: String?, @Body movie: Movie): Call<ApiResponse<Movie>>

    // Delete a movie by ID
    @DELETE("movie/delete/{id}")
    fun deleteMovie(@Path("id") id: String?): Call<ApiResponse<String>>

    // Register a User
    @POST("register")
    fun registerUser(@Body newUser: MyUser): Call<ApiResponse<MyUser>>

    // Login a User
    @POST("login")
    fun loginUser(@Body credentials: MyUser): Call<ApiResponse<MyUser>>
}