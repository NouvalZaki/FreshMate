package com.example.freshmate.data.helper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freshmate.data.response.DataFruit
import com.example.freshmate.databinding.HomePreviewCardBinding

class HomeFruitListAdapter(private var listFruits: List<DataFruit> = listOf()) : RecyclerView.Adapter<HomeFruitListAdapter.HomeFruitViewHolder>() {
    private var onItemClickCallback: ((Int) -> Unit)? = null  // Change to Int since id is Int

    inner class HomeFruitViewHolder(private val binding: HomePreviewCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(fruit: DataFruit) {
            binding.tvFruitPreview.text = fruit.fruitName
            Glide.with(binding.root.context)
                .load(fruit.fruitImagePreview)
                .into(binding.imgPreview)

            binding.homePreviewCard.setOnClickListener {
                fruit.id.let { id ->
                    onItemClickCallback?.invoke(id)  // Pass id as Int
                }
            }
        }
    }
    fun setOnItemClickCallback(callback: (Int) -> Unit) {  // Change to (Int) callback
        this.onItemClickCallback = callback
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeFruitListAdapter.HomeFruitViewHolder {
        val binding = HomePreviewCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeFruitViewHolder(binding)
    }
    override fun onBindViewHolder(holder: HomeFruitViewHolder, position: Int) {
        holder.bind(listFruits[position])
    }
    override fun getItemCount(): Int = listFruits.size

    fun updateData(newFruit: List<DataFruit>) {
        listFruits = newFruit
        notifyDataSetChanged()
    }
}