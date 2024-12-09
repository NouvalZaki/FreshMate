package com.example.freshmate.ui.fruitDiseases

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freshmate.data.helper.DiseaseAdapter
import com.example.freshmate.databinding.FragmentDiseasesBinding


class DiseasesFragment : Fragment() {
    private var _binding: FragmentDiseasesBinding? = null
    private val binding get() = _binding!!

    private lateinit var diseaseAdapter: DiseaseAdapter
    val DiseaseViewModel: DiseasesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiseasesBinding.inflate(inflater, container, false)

        binding.rvFruitDisease.layoutManager = LinearLayoutManager(requireContext())
        diseaseAdapter= DiseaseAdapter(listOf())
        binding.rvFruitDisease.adapter = diseaseAdapter

        diseaseAdapter.setOnItemClickCallback { diseaseId ->
            val intent = Intent(requireActivity(), DetailActivityDisease::class.java)
            intent.putExtra("DISEASE_ID", diseaseId)  // Passing the id as Int
            startActivity(intent)
        }

        observeDisease()

        return binding.root

    }

    private fun observeDisease() {
        DiseaseViewModel.listOfDisease.observe(viewLifecycleOwner) { fruits ->
            if (fruits != null && fruits.isNotEmpty()){
                diseaseAdapter.updateData(fruits)
            }else{
                Toast.makeText(requireContext(), "No Disease available", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}