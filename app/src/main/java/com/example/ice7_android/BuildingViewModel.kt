package com.example.ice7_android

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BuildingViewModel(application: Application) : AndroidViewModel(application) {
    private val buildingList = MutableLiveData<List<Building>>()
    val buildings: LiveData<List<Building>> = buildingList

    private val individualBuilding = MutableLiveData<Building>()
    val building: LiveData<Building> = individualBuilding

    private val dataManager = DataManager.instance(application)

    fun getAllBuildings() {
        dataManager.getAllBuildings(object : Callback<ApiResponse<List<Building>>> {
            override fun onResponse(call: Call<ApiResponse<List<Building>>>, response: Response<ApiResponse<List<Building>>>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.success) {
                        buildingList.postValue(apiResponse.data)
                    } else {
                        println("API Access Error: ${apiResponse?.message}")
                    }
                } else {
                    println("Response NOT successful Code: ${response.code()} Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Building>>>, t: Throwable) {
                println("getAllBuildings failed with error: ${t.message}")
            }
        })
    }

    fun getBuildingById(id: String) {
        dataManager.getBuildingById(id, object : Callback<ApiResponse<Building>> {
            override fun onResponse(call: Call<ApiResponse<Building>>, response: Response<ApiResponse<Building>>) {
                if (response.isSuccessful) {
                    individualBuilding.postValue(response.body()?.data)
                } else {
                    println("Error getting building by id: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<Building>>, t: Throwable) {
                println("Failure getting building by id: ${t.message}")
            }
        })
    }

    fun addBuilding(building: Building) {
        dataManager.addBuilding(building, object : Callback<ApiResponse<Building>> {
            override fun onResponse(call: Call<ApiResponse<Building>>, response: Response<ApiResponse<Building>>) {
                if (response.isSuccessful) {
                    getAllBuildings() // Re-fetch buildings to update UI
                } else {
                    println("Error adding building: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<Building>>, t: Throwable) {
                println("Failure adding building: ${t.message}")
            }
        })
    }

    fun updateBuilding(id: String, updatedBuilding: Building) {
        dataManager.updateBuilding(id, updatedBuilding, object : Callback<ApiResponse<Building>> {
            override fun onResponse(call: Call<ApiResponse<Building>>, response: Response<ApiResponse<Building>>) {
                if (response.isSuccessful) {
                    getAllBuildings() // Re-fetch buildings to update UI
                } else {
                    println("Error updating building: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<Building>>, t: Throwable) {
                println("Failure updating building: ${t.message}")
            }
        })
    }

    fun deleteBuilding(id: String) {
        dataManager.deleteBuilding(id, object : Callback<ApiResponse<String>> {
            override fun onResponse(call: Call<ApiResponse<String>>, response: Response<ApiResponse<String>>) {
                if (response.isSuccessful) {
                    getAllBuildings() // Re-fetch buildings to update UI
                } else {
                    println("Error deleting building: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                println("Failure deleting building: ${t.message}")
            }
        })
    }
}
