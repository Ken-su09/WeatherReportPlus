package com.suonk.weatherreportplus.ui.activities

import android.Manifest
import android.graphics.drawable.AnimationDrawable
import android.location.*
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.suonk.weatherreportplus.databinding.ActivityMainBinding
import com.suonk.weatherreportplus.utils.CheckAndRequestPermissions.checkAndRequestPermission
import com.suonk.weatherreportplus.viewmodels.WeatherReportViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 10100
    }

    private lateinit var binding: ActivityMainBinding
    private var locationManager: LocationManager? = null

    private val viewModel: WeatherReportViewModel by viewModels()

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            getCityFromLatLong(location.latitude, location.longitude)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?

        trackLocationIfPermissionIsGranted()

        binding.buttonGetCurrentWeather.setOnClickListener {
            buttonClickAnimation()
            initProgressBar()
            getLocationManager()
        }
    }

    private fun getLocationManager() {
        try {
            locationManager?.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0L,
                0F,
                locationListener
            )
        } catch (ex: SecurityException) {
            Log.d("myTag", "Security Exception, no location available")
        }
    }

    private fun getCityFromLatLong(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(this, Locale.getDefault())
        val listOfCitiesName = geocoder.getFromLocation(latitude, longitude, 1)
        if (listOfCitiesName.size > 0) {
            getCurrentWeatherByCurrentLocation(listOfCitiesName[0])
        }
    }

    private fun getCurrentWeatherByCurrentLocation(address: Address?) {
        viewModel.getWeatherStackData(address!!.locality)

        binding.cityName.text = address.locality

        viewModel.weatherStackLiveData.observe(this, { weatherStackData ->
            Log.i("getCurrentWeather", "${weatherStackData.current.temperature}")
            Log.i("getCurrentWeather", "${weatherStackData.current.humidity}")

            binding.temperatureValue.text = "${weatherStackData.current.temperature} °C"
            binding.windValue.text = "${weatherStackData.current.wind_speed} km/h"
            binding.humidityValue.text = "${weatherStackData.current.humidity} %"

            Glide.with(this)
                .load(weatherStackData.current.weather_icons[0])
                .centerCrop()
                .into(binding.weatherIcon)
        })
    }

    private fun trackLocationIfPermissionIsGranted() {
        checkAndRequestPermission(
            Manifest.permission.ACCESS_FINE_LOCATION,
            REQUEST_LOCATION_PERMISSION,
            ::getLocationManager
        )
    }

    private fun initProgressBar() {
        viewModel.loadingProgressBar.observe(this, { isVisible ->
            binding.progressBar.isVisible = isVisible
        })
    }

    private fun buttonClickAnimation() {
        val frameAnimation = binding.buttonGetCurrentWeather.drawable as AnimationDrawable
        frameAnimation.start()
        Handler(Looper.getMainLooper()).postDelayed({
            frameAnimation.stop()
        }, 390)
    }
}