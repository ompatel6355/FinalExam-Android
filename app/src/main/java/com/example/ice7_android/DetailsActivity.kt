package com.example.ice7_android

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.ice7_android.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private val viewModel: BuildingViewModel by viewModels()
    private var isUpdate: Boolean = false
    private var buildingId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve extras from intent
        isUpdate = intent.getBooleanExtra("IS_UPDATE", false)
        buildingId = intent.getStringExtra("BUILDING_ID")

        // Fetch building details if in update mode
        if (isUpdate && buildingId != null) {
            viewModel.getBuildingById(buildingId!!)
        }

        // Observe building data and populate UI
        viewModel.building.observe(this) { building ->
            building?.let {
                binding.nameEditText.setText(it.name ?: "")
                binding.typeEditText.setText(it.type ?: "")
                binding.dateBuiltEditText.setText(it.dateBuilt ?: "")
                binding.cityEditText.setText(it.city ?: "")
                binding.countryEditText.setText(it.country ?: "")
                binding.descriptionMultiLine.setText(it.description ?: "")
                binding.architectsEditText.setText(it.architects?.joinToString(", ") ?: "")
                binding.costEditText.setText(it.cost ?: "")
                binding.websiteEditText.setText(it.website ?: "")
                binding.imageUrlEditText.setText(it.imageURL ?: "")
            }
        }

        // Handle update/add building
        binding.updateButton.setOnClickListener {
            val building = Building(
                name = binding.nameEditText.text.toString().takeIf { it.isNotEmpty() } ?: "",
                type = binding.typeEditText.text.toString().takeIf { it.isNotEmpty() } ?: "",
                dateBuilt = binding.dateBuiltEditText.text.toString().takeIf { it.isNotEmpty() } ?: "",
                city = binding.cityEditText.text.toString().takeIf { it.isNotEmpty() } ?: "",
                country = binding.countryEditText.text.toString().takeIf { it.isNotEmpty() } ?: "",
                description = binding.descriptionMultiLine.text.toString().takeIf { it.isNotEmpty() } ?: "",
                architects = binding.architectsEditText.text.toString().split(",").map { it.trim() }.takeIf { it.isNotEmpty() } ?: emptyList(),
                cost = binding.costEditText.text.toString().takeIf { it.isNotEmpty() } ?: "",
                website = binding.websiteEditText.text.toString().takeIf { it.isNotEmpty() } ?: "",
                imageURL = binding.imageUrlEditText.text.toString().takeIf { it.isNotEmpty() } ?: ""
            )
            if (isUpdate) {
                buildingId?.let { id ->
                    viewModel.updateBuilding(id, building)
                }
            } else {
                viewModel.addBuilding(building)
            }
            setResult(RESULT_OK) // Indicate successful update/add
            finish()
        }

        // Handle cancel action
        binding.cancelButton.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}
