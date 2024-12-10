package com.example.freshmate.ui.home

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freshmate.R
import com.example.freshmate.data.helper.HomeDiseasesListAdapter
import com.example.freshmate.data.helper.HomeFruitListAdapter
import com.example.freshmate.databinding.FragmentHomeBinding
import com.example.freshmate.ui.fruitDiseases.DetailActivityDisease
import com.example.freshmate.ui.fruitDiseases.DiseasesFragment
import com.example.freshmate.ui.fruitList.DetailActivityFruitList
import com.example.freshmate.ui.fruitList.FruitListFragment
import com.jem.blobbackground.model.Blob
import kotlin.math.min

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeFruitPreviewAdapter: HomeFruitListAdapter
    private lateinit var homeDiseasePreviewAdapter: HomeDiseasesListAdapter
    val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.rvPreviewFruit.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        homeFruitPreviewAdapter= HomeFruitListAdapter(listOf())
        binding.rvPreviewFruit.adapter = homeFruitPreviewAdapter

        binding.rvPreviewDiseases.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        homeDiseasePreviewAdapter= HomeDiseasesListAdapter(listOf())
        binding.rvPreviewDiseases.adapter = homeDiseasePreviewAdapter

        homeDiseasePreviewAdapter.setOnItemClickCallback { diseaseId ->
            val intent = Intent(requireActivity(), DetailActivityDisease::class.java)
            intent.putExtra("DISEASE_ID", diseaseId)  // Passing the id as Int
            startActivity(intent)
        }

        homeFruitPreviewAdapter.setOnItemClickCallback { fruitId ->
            val intent = Intent(requireActivity(), DetailActivityFruitList::class.java)
            intent.putExtra("FRUIT_ID", fruitId)  // Passing the id as Int
            startActivity(intent)

        }
        observeFruit()
        observeDisease()

        binding?.blobFrameLayout?.post {
            val layoutWidth = binding.blobFrameLayout.width.toFloat()
            val layoutHeight = binding.blobFrameLayout.height.toFloat()
            val minVal = min(layoutWidth, layoutHeight)
            binding.blobFrameLayout.removeBlobs()
            binding.blobFrameLayout.addBlob(
                Blob.Configuration(
                    pointCount = 32,
                    blobCenterPosition = PointF(0f, 0f),
                    radius = minVal - minVal / 4,
                    maxOffset = (minVal - minVal / 4) / 12,
                    paint = getPaint(Color.parseColor("#1F3701"))
                ),
                Blob.Configuration(
                    pointCount = 12,
                    blobCenterPosition = PointF(
                        layoutWidth,
                        if (layoutWidth < layoutHeight) layoutHeight / 4 else 0f
                    ),
                    radius = minVal / 2,
                    maxOffset = (minVal / 4) / 8,
                    shapeAnimationDuration = 1500,
                    paint = getPaint(Color.parseColor("#354E16"))
                ),
                Blob.Configuration(
                    pointCount = 24,
                    blobCenterPosition = PointF((layoutWidth * 2) / 3, layoutHeight),
                    radius = minVal - minVal / 5,
                    maxOffset = (minVal - minVal / 8) / 8,
                    paint = getPaint(Color.parseColor("#CDEDA3"))
                )
            )
        }

        initListener()
        return binding.root
    }

    private fun initListener() = with(binding) {
        btnViewAllDisease.setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_diagnose)
        }
        btnViewAllFruitList.setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_fruit)
        }
    }

    private fun getPaint(paintColor: Int): Paint {
        return Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = paintColor
        }
    }

    private fun observeDisease() {
        homeViewModel.listOfDisease.observe(viewLifecycleOwner) { diseases ->
            if (diseases != null && diseases.isNotEmpty()){
                val previewDiseases = diseases.take(4)
                homeDiseasePreviewAdapter.updateData(previewDiseases)
            }else{
                Toast.makeText(requireContext(), "Tidak Ada List Penyakit Yang Tersedia", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeFruit() {
        homeViewModel.listOfFruit.observe(viewLifecycleOwner) { fruits ->
            if (fruits != null && fruits.isNotEmpty()){
                val previewFruits = fruits.take(5)
                homeFruitPreviewAdapter.updateData(previewFruits)
            }else {
                Toast.makeText(requireContext(), "No Fruit available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}