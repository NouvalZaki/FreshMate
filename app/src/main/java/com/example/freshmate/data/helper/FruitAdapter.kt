package com.example.freshmate.data.helper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.freshmate.data.response.DataFruit
import com.example.freshmate.databinding.FruitListCardBinding
import com.bumptech.glide.Glide

class FruitAdapter(
    private var listFruits: List<DataFruit> = listOf()
) : RecyclerView.Adapter<FruitAdapter.EventViewHolder>() {

    private var onItemClickCallback: ((Int) -> Unit)? = null  // Change to Int since id is Int

    // ViewHolder class to hold item views
    inner class EventViewHolder(private val binding: FruitListCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(fruit: DataFruit) {
            // Set data to UI components
            binding.fruitName.text = fruit.fruitName
            binding.fruitDesc.text = fruit.fruitDesc
            Glide.with(binding.root.context)
                .load(fruit.fruitImagePreview)
                .into(binding.fruitImagePreview)

            // Handle item click
            binding.cardMantap.setOnClickListener {
                fruit.id?.let { id ->
                    onItemClickCallback?.invoke(id)  // Pass id as Int
                }
            }
        }
    }

    // Define the callback for item clicks
    fun setOnItemClickCallback(callback: (Int) -> Unit) {  // Change to (Int) callback
        this.onItemClickCallback = callback
    }

    // Create ViewHolder from XML layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = FruitListCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    // Bind data to ViewHolder
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(listFruits[position])
    }

    // Return item count
    override fun getItemCount(): Int = listFruits.size

    // Update RecyclerView data
    fun updateData(newEvents: List<DataFruit>) {
        listFruits = newEvents
        notifyDataSetChanged()
    }
}
