package com.example.freshmate.data.helper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freshmate.data.response.DataDisease
import com.example.freshmate.data.response.DataFruit
import com.example.freshmate.databinding.DiseaseCardBinding
import com.example.freshmate.databinding.HomeDieasesPreviewCardBinding
import com.example.freshmate.databinding.HomePreviewCardBinding

class HomeDiseasesListAdapter(private var listDiseases: List<DataDisease> = listOf()
) : RecyclerView.Adapter<HomeDiseasesListAdapter.DiseaseViewHolder>() {
    private var onItemClickCallback: ((Int) -> Unit)? = null  // Change to Int since id is Int

   inner class DiseaseViewHolder(private val binding: DiseaseCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(fruit: DataDisease) {
            Glide.with(binding.root.context)
                .load(fruit.dieasesPreview)
                .into(binding.diseaseImage)
            binding.diseaseTitle.text = fruit.diseasesName
            binding.diseaseDescription.text = fruit.diseasesDesc
            binding.diseaseCard.setOnClickListener {
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
    ): HomeDiseasesListAdapter.DiseaseViewHolder {
        val binding = DiseaseCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiseaseViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HomeDiseasesListAdapter.DiseaseViewHolder,
        position: Int
    ) {
        holder.bind(listDiseases[position])
    }

    override fun getItemCount(): Int = listDiseases.size

    fun updateData(newDiseases: List<DataDisease>) {
        listDiseases= newDiseases
        notifyDataSetChanged()
    }
}

