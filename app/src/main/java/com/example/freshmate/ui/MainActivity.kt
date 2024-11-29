package com.example.freshmate.ui
import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.freshmate.R
import com.example.freshmate.databinding.ActivityMainBinding
import com.example.freshmate.ui.camera.CameraActivity
import com.example.freshmate.ui.fruitList.FruitListFragment
import com.example.freshmate.ui.home.HomeFragment
import com.example.freshmate.ui.setting.SettingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeFragment = HomeFragment()
        val fruitListFragment = FruitListFragment()
        val settingsFragment = SettingFragment()

        makeCurrentFragment(homeFragment)

        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> makeCurrentFragment(homeFragment)
                R.id.navigation_fruit -> makeCurrentFragment(fruitListFragment)
                R.id.navigation_settings -> makeCurrentFragment(settingsFragment)
            }
            true
        }

        binding.fabCamera.setOnClickListener {
            Intent(this, CameraActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_wrapper, fragment)
            commit()
        }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
