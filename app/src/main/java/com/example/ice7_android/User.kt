package com.example.ice7_android
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MyUser(
    @Json(name = "username") val username: String,
    @Json(name = "emailAddress") val emailAddress: String? = null,
    @Json(name = "displayName") val displayName: String? = null,
    // Optional for use during registration only
    @Json(name= "firstName") val firstName: String? = null,
    @Json(name= "lastName") val lastName: String? = null,
    @Json(name = "password") val password: String? = null
)