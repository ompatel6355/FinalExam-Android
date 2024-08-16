package com.example.ice7_android

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Building(
    @Json(name = "_id") val id: String? = null,
    @Json(name = "name") val name: String, // Building name
    @Json(name = "type") val type: String, // Building type
    @Json(name = "dateBuilt") val dateBuilt: String? = null, // Date built (e.g., "YYYY-MM-DD")
    @Json(name = "city") val city: String? = null, // City where the building is located
    @Json(name = "country") val country: String? = null, // Country where the building is located
    @Json(name = "description") val description: String? = null, // Description of the building
    @Json(name = "architects") val architects: List<String>? = null, // List of architects
    @Json(name = "cost") val cost: String? = null, // Cost of construction
    @Json(name = "website") val website: String? = null, // URL of the building's website
    @Json(name = "imageURL") val imageURL: String? = null // URL of an image of the building
)
