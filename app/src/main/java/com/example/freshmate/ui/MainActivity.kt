package com.example.freshmate.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.freshmate.R
import com.example.freshmate.databinding.ActivityMainBinding
import com.example.freshmate.ui.camera.CameraActivity
import com.example.freshmate.ui.fruitList.FruitListFragment
import com.example.freshmate.ui.home.HomeFragment
import com.example.freshmate.ui.setting.SettingFragment

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

//        val homeFragment = HomeFragment()
//        val fruitListFragment = FruitListFragment()
//        val settingsFragment = SettingFragment()
//
//        makeCurrentFragment(homeFragment)
//
//        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnNavigationItemSelectedListener {
//            when (it.itemId) {
//                R.id.navigation_home -> makeCurrentFragment(homeFragment)
//                R.id.navigation_fruit -> makeCurrentFragment(fruitListFragment)
//                R.id.navigation_settings -> makeCurrentFragment(settingsFragment)
//            }
//            true
//        }
        binding.fabCamera.setOnClickListener {
            Intent(this, CameraActivity::class.java).also {
                startActivity(it)
            }
        }

        setupNavigation()
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_wrapper) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)
    }


//    private fun makeCurrentFragment(fragment: Fragment) =
//        supportFragmentManager.beginTransaction().apply {
//            replace(R.id.fragment_wrapper, fragment)
//            commit()
//        }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}