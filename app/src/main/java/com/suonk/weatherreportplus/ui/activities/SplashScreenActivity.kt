package com.suonk.weatherreportplus.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import com.suonk.weatherreportplus.R
import com.suonk.weatherreportplus.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeUI()
        delayedToMainActivity()
    }

    private fun initializeUI() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.suddenly_appear)
        binding.appLogo.startAnimation(animation)
        binding.appName.startAnimation(animation)
    }

    private fun delayedToMainActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
            finish()
        }, 4000)
    }
}