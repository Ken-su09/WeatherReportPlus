package com.suonk.weatherreportplus.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.suonk.weatherreportplus.R
import com.suonk.weatherreportplus.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.buttonToMaps.setOnClickListener {
            startActivity(Intent(this@MainActivity, MapsActivity::class.java))
        }
    }
}