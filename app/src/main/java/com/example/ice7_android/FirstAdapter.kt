package com.example.ice7_android

import com.example.ice7_android.databinding.ItemLayoutBinding // Updated to match your XML file
import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class FirstAdapter(private var dataSet: List<Building>) :
    RecyclerView.Adapter<FirstAdapter.ViewHolder>() {

    var onBuildingClick: ((Building) -> Unit)? = null

    class ViewHolder(val binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val building = dataSet[position]
        viewHolder.binding.title.text = building.name
        viewHolder.binding.description.text = building.type // Assuming 'description' is the correct ID for the type

        viewHolder.binding.root.setOnClickListener {
            onBuildingClick?.invoke(dataSet[position])
        }

        val cost = building.cost?.toDoubleOrNull()

//        if (cost != null) {
//            viewHolder.binding.rating.text = cost.toString()
//
//            // Set background drawable based on cost (or any other attribute)
//            when {
//                cost > 1000000 -> { // Example threshold for high cost
//                    viewHolder.binding.rating.setBackgroundResource(R.drawable.background_green)
//                    viewHolder.binding.rating.setTextColor(Color.BLACK)
//                }
//                cost > 500000 -> { // Example threshold for medium cost
//                    viewHolder.binding.rating.setBackgroundResource(R.drawable.background_yellow)
//                    viewHolder.binding.rating.setTextColor(Color.BLACK)
//                }
//                else -> {
//                    viewHolder.binding.rating.setBackgroundResource(R.drawable.background_red)
//                    viewHolder.binding.rating.setTextColor(Color.WHITE)
//                }
//            }
//        } else {
//            viewHolder.binding.rating.text = "N/A"
//            viewHolder.binding.rating.setBackgroundResource(R.drawable.background_gray)
//            viewHolder.binding.rating.setTextColor(Color.WHITE)
//        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateBuildings(newBuildings: List<Building>) {
        dataSet = newBuildings
        notifyDataSetChanged()
    }
}
