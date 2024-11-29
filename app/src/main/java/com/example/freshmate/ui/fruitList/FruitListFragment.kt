package com.example.freshmate.ui.fruitList

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freshmate.R
import com.example.freshmate.data.helper.FruitAdapter
import com.example.freshmate.databinding.FragmentFruitListBinding


class FruitListFragment : Fragment() {
    private var _binding: FragmentFruitListBinding? = null
    private val binding get() = _binding!!

    private lateinit var fruitAdapter: FruitAdapter
    val fruitlistViewModel: FruitListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFruitListBinding.inflate(inflater, container, false)

        binding.rvFruitList.layoutManager = LinearLayoutManager(requireContext())
        fruitAdapter= FruitAdapter(listOf())
        binding.rvFruitList.adapter = fruitAdapter

        fruitAdapter.setOnItemClickCallback { fruitId ->
            val intent = Intent(requireActivity(), DetailActivityFruitList::class.java)
            intent.putExtra("FRUIT_ID", fruitId)  // Passing the id as Int
            startActivity(intent)
        }

        observeFruit()

        return binding.root

    }

    private fun observeFruit() {
        fruitlistViewModel.listOfFruit.observe(viewLifecycleOwner) { fruits ->
            if (fruits != null && fruits.isNotEmpty()){
                fruitAdapter.updateData(fruits)
            }else{
                Toast.makeText(requireContext(), "No Fruit available", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}