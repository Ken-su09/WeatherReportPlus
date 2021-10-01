package com.suonk.weatherreportplus.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.suonk.weatherreportplus.databinding.ActivityMainBinding
import com.suonk.weatherreportplus.navigation.Navigator
import com.suonk.weatherreportplus.navigation.WeatherReportFlowCoordinator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

        CoroutineScope(Dispatchers.Main).launch {
            delay(1500)
            coordinator.start()
        }
        delayedToMainFragment()
    }

    private fun delayedToMainFragment() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(6000)
            coordinator.showCurrentWeather()
        }
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