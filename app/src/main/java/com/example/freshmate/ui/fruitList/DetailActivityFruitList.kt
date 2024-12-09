package com.example.freshmate.ui.fruitList

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.freshmate.data.helper.FruitAdapter
import com.example.freshmate.databinding.ActivityDetailFruitListBinding

class DetailActivityFruitList : AppCompatActivity() {
    private lateinit var binding: ActivityDetailFruitListBinding
    private lateinit var fruitAdapter: FruitAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFruitListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Enable the back button
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Handle back button press
        }

        val detailFruitListViewModel: DetailActivityListViewModel by viewModels()

        // Observe the LiveData for fruit details
        detailFruitListViewModel.fruitListDetail.observe(this, Observer { detailResponse ->
            detailResponse?.let { fruit ->
                // Populate the UI with the fruit details
                binding.title.text = fruit.fruitName ?: "Unknown Fruit"
                binding.description.text =
                    HtmlCompat.fromHtml(fruit.fruitDesc ?: "No description available", HtmlCompat.FROM_HTML_MODE_LEGACY)
                Glide.with(this)
                    .load(fruit.fruitImageDetail)
                    .into(binding.picture)

            } ?: run {
                // Handle the case where the fruit details are null
                Log.e("DetailActivity", "Fruit details are null")
                Toast.makeText(this, "Failed to load fruit details", Toast.LENGTH_SHORT).show()
            }
        })

        // Fetch fruit details using the ViewModel
        val fruitId = intent.getIntExtra("FRUIT_ID", -1)
        if (fruitId != -1) {
            detailFruitListViewModel.fetchDetailFruit(fruitId)
        } else {
            Log.e("DetailActivity", "Invalid Fruit ID")
            Toast.makeText(this, "Invalid fruit ID provided", Toast.LENGTH_SHORT).show()
        }
    }
}









