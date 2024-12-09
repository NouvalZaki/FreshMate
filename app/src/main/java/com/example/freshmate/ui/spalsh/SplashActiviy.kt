package com.example.freshmate.ui.spalsh

import android.annotation.SuppressLint
import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dotlottie.dlplayer.Mode
import com.example.freshmate.R
import com.example.freshmate.databinding.ActivitySplashActiviyBinding
import com.example.freshmate.ui.MainActivity
import com.lottiefiles.dotlottie.core.model.Config
import com.lottiefiles.dotlottie.core.util.DotLottieEventListener
import com.lottiefiles.dotlottie.core.util.DotLottieSource

@SuppressLint("CustomSplashScreen")
class SplashActiviy : AppCompatActivity() {
    private val binding by lazy { ActivitySplashActiviyBinding.inflate(layoutInflater) }
    private var isTransitionStarted = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        initView()
    }
    private val eventListener = object : DotLottieEventListener {
        override fun onComplete() {
            super.onComplete()
            if (!isTransitionStarted && !isFinishing) {
                isTransitionStarted = true
                navigateToMain()
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun navigateToMain() {
        Intent(this, MainActivity::class.java).also { intent ->
            startActivity(intent)
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    private fun initView() {
        binding.lottieSplash.apply {
            val config = Config.Builder().autoplay(true).loop(false).playMode(Mode.FORWARD)
                .source(DotLottieSource.Asset("SplashAnimation.json")).useFrameInterpolation(true)
                .speed(1f).build()

            load(config)
            addEventListener(eventListener)
        }
    }


    override fun onPause() {
        binding.lottieSplash.pause()
        super.onPause()
    }

    override fun onDestroy() {
        binding.lottieSplash.removeEventListener(eventListener)
        super.onDestroy()
    }

}