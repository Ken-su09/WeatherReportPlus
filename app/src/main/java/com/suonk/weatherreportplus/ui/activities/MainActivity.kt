package com.suonk.weatherreportplus.ui.activities

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.suonk.weatherreportplus.databinding.ActivityMainBinding
import com.suonk.weatherreportplus.navigation.Navigator
import com.suonk.weatherreportplus.navigation.WeatherReportFlowCoordinator
import com.suonk.weatherreportplus.utils.CheckAndRequestPermissions.checkAndRequestPermission
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

        Handler(Looper.getMainLooper()).postDelayed({
            coordinator.start()
        }, 1500)
        delayedToMainFragment()
    }

    private fun delayedToMainFragment() {
        Handler(Looper.getMainLooper()).postDelayed({
            coordinator.showCurrentWeather()
        }, 6000)
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