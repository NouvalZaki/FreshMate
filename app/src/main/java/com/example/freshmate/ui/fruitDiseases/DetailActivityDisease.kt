package com.example.freshmate.ui.fruitDiseases

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.freshmate.data.helper.DiseaseAdapter
import com.example.freshmate.databinding.ActivityDetailDiseaseBinding


class DetailActivityDisease : AppCompatActivity() {
    private lateinit var binding: ActivityDetailDiseaseBinding
    private lateinit var diseaseAdapter: DiseaseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDiseaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Enable the back button
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Handle back button press
        }

        val detailDiseaseViewModel: DetailDiseasesViewModel by viewModels()

        // Observe the LiveData for fruit details
        detailDiseaseViewModel.diseaseDetail.observe(this, Observer { detailResponse ->
            detailResponse?.let { disease ->
                // Populate the UI with the fruit details
                binding.title.text = disease.diseasesName ?: "Unknown Disease"
                binding.description.text =
                    HtmlCompat.fromHtml(disease.diseasesDesc ?: "No description available", HtmlCompat.FROM_HTML_MODE_LEGACY)
                Glide.with(this)
                    .load(disease.diseasesDetail)
                    .into(binding.picture)

            } ?: run {
                // Handle the case where the fruit details are null
                Log.e("DetailActivity", "Disease details are null")
                Toast.makeText(this, "Failed to load diseases details", Toast.LENGTH_SHORT).show()
            }
        })

        // Fetch fruit details using the ViewModel
        val diseaseId = intent.getIntExtra("DISEASE_ID", -1)
        if (diseaseId != -1) {
            detailDiseaseViewModel.fetchDetailDisease(diseaseId)
        } else {
            Log.e("DetailActivity", "Invalid Disease ID")
            Toast.makeText(this, "Invalid Disease ID provided", Toast.LENGTH_SHORT).show()
        }
    }
}