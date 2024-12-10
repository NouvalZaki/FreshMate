package com.example.freshmate.data.helper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freshmate.data.response.DataDisease
import com.example.freshmate.databinding.DiseaseCardBinding

class DiseaseAdapter (
    private var listDiseases: List<DataDisease> = listOf()
) : RecyclerView.Adapter<DiseaseAdapter.DiseaseViewHolder>() {

    private var onItemClickCallback: ((Int) -> Unit)? = null  // Change to Int since id is Int

    // ViewHolder class to hold item views
    inner class DiseaseViewHolder(private val binding: DiseaseCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(disease: DataDisease) {
            // Set data to UI components
            binding.diseaseTitle.text = disease.diseasesName
            binding.diseaseDescription.text = disease.diseasesDesc
            Glide.with(binding.root.context)
                .load(disease.dieasesPreview)
                .into(binding.diseaseImage)

            // Handle item click
            binding.diseaseCard.setOnClickListener {
                disease.id?.let { id ->
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):DiseaseViewHolder {
        val binding = DiseaseCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiseaseViewHolder(binding)
    }

    // Bind data to ViewHolder
    override fun onBindViewHolder(holder: DiseaseViewHolder, position: Int) {
        holder.bind(listDiseases[position])
    }

    // Return item count
    override fun getItemCount(): Int = listDiseases.size

    // Update RecyclerView data
    fun updateData(newEvents: List<DataDisease>) {
        listDiseases = newEvents
        notifyDataSetChanged()
    }
}