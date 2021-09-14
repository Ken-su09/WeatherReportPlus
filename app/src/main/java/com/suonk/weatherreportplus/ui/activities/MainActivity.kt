package com.suonk.weatherreportplus.ui.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.suonk.weatherreportplus.databinding.ActivityMainBinding
import com.suonk.weatherreportplus.navigation.Navigator
import com.suonk.weatherreportplus.navigation.WeatherReportFlowCoordinator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navigator: Navigator
    private lateinit var coordinator: WeatherReportFlowCoordinator
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigator.activity = this
        coordinator = WeatherReportFlowCoordinator(navigator)

        coordinator.start()
        delayedToMainFragment()
    }

    private fun delayedToMainFragment() {
        Handler(Looper.getMainLooper()).postDelayed({
            coordinator.showCurrentWeather()
        }, 4000)
    }

    fun goToWeatherDetail() {
        coordinator.showWeatherDetails()
    }

    fun backToCurrentWeather() {
        coordinator.showCurrentWeather()
    }

    override fun onDestroy() {
        super.onDestroy()
        navigator.activity = null
    }
}