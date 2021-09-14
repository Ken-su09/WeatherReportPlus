package com.suonk.weatherreportplus.ui.fragments

import android.Manifest
import android.graphics.drawable.AnimationDrawable
import android.location.*
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.suonk.weatherreportplus.databinding.FragmentCurrentWeatherBinding
import com.suonk.weatherreportplus.utils.CheckAndRequestPermissions.checkAndRequestPermission
import com.suonk.weatherreportplus.viewmodels.WeatherReportViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CurrentWeatherFragment : Fragment() {

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 10100
    }

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

    private lateinit var binding: FragmentCurrentWeatherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)
        initializeUI()
        return binding.root
    }

    private fun initializeUI() {
        locationManager =
            requireActivity().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager?

        trackLocationIfPermissionIsGranted()

        binding.buttonGetCurrentWeather.setOnClickListener {
            binding.buttonGetCurrentWeather.isEnabled = false
            buttonClickAnimation()
            initProgressBar()
            getLocationManager()

            Handler(Looper.getMainLooper()).postDelayed({
                binding.buttonGetCurrentWeather.isEnabled = true
                binding.buttonGetCurrentWeatherGrey.isVisible = false
                binding.buttonGetCurrentWeather.isVisible = true
            }, 4000)
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
        val geocoder = Geocoder(context, Locale.getDefault())
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

            binding.weatherDescription.text = weatherStackData.current.weather_descriptions[0]
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
        requireActivity().checkAndRequestPermission(
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
            binding.buttonGetCurrentWeatherGrey.isVisible = true
            binding.buttonGetCurrentWeather.isVisible = false
            frameAnimation.stop()
        }, 400)
    }
}