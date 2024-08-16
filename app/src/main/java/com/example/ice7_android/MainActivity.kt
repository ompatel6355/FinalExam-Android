package com.example.ice7_android

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ice7_android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: BuildingViewModel by viewModels()
    private lateinit var firstAdapter: FirstAdapter
    private lateinit var buildingList: MutableList<Building>
    private lateinit var sharedPreferences: SharedPreferences

    private val detailsActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            viewModel.getAllBuildings() // Refresh the building list
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)

        // Initialize the adapter with an empty list
        firstAdapter = FirstAdapter(emptyList())
        binding.FirstRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = firstAdapter
        }

        viewModel.buildings.observe(this) { buildings ->
            buildingList = buildings.toMutableList()
            firstAdapter.updateBuildings(buildings)
        }

        firstAdapter.onBuildingClick = { building ->
            val intent = Intent(this, DetailsActivity::class.java).apply {
                putExtra("BUILDING_ID", building.id)
                putExtra("IS_UPDATE", true)
            }
            detailsActivityResultLauncher.launch(intent)
        }

        binding.addButton.setOnClickListener {
            // Go to the Details Activity for adding a new building
            val intent = Intent(this, DetailsActivity::class.java).apply {
                putExtra("IS_UPDATE", false)
            }
            detailsActivityResultLauncher.launch(intent)
        }

        binding.logoutButton.setOnClickListener {
            // Remove the auth_token
            val editor = sharedPreferences.edit()
            editor.remove("auth_token") // Use remove instead of putting an empty string
            editor.apply()

            // Navigate back to the Login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Setup swipe to delete
        val swipeToDeleteCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false // Not used
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                AlertDialog.Builder(this@MainActivity).apply {
                    setTitle(getString(R.string.delete_building))
                    setMessage(getString(R.string.are_you_sure))
                    setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                        dialog.dismiss()
                        val position = viewHolder.adapterPosition
                        val buildingId = buildingList[position].id
                        if (buildingId != null) {
                            viewModel.deleteBuilding(buildingId) // Trigger deletion in ViewModel
                        } else {
                            println("Building ID is null, cannot delete.")
                        }                    }
                    setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                        dialog.dismiss()
                        firstAdapter.notifyItemChanged(viewHolder.adapterPosition) // Reverts the swipe action visually
                    }
                    setCancelable(false)
                }.create().show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.FirstRecyclerView)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllBuildings() // Refresh the building list
    }
}
