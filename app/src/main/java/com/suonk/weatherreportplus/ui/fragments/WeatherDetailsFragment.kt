package com.suonk.weatherreportplus.ui.fragments

import android.Manifest
import android.graphics.drawable.AnimationDrawable
import android.location.*
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.suonk.weatherreportplus.databinding.FragmentWeatherDetailsBinding
import com.suonk.weatherreportplus.ui.activities.MainActivity
import com.suonk.weatherreportplus.utils.CheckAndRequestPermissions.checkAndRequestPermission
import com.suonk.weatherreportplus.viewmodels.WeatherReportViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class WeatherDetailsFragment : Fragment() {

    //region ========================================== Val or Var ==========================================

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 10100
    }

    private var locationManager: LocationManager? = null

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            getCityFromLatLong(location.latitude, location.longitude)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    private lateinit var binding: FragmentWeatherDetailsBinding

    private val viewModel: WeatherReportViewModel by activityViewModels()

    //endregion

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherDetailsBinding.inflate(inflater, container, false)
//        initializeUI()

        viewModel.locationLiveData.observe(viewLifecycleOwner, { currentLocation ->
            Log.i("locationLiveData", currentLocation)
        })

        return binding.root
    }

    private fun initializeUI() {
        locationManager =
            requireActivity().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager?

        trackLocationIfPermissionIsGranted()

        binding.buttonGetCurrentWeather.setOnClickListener {
            getWeatherButtonClick()
        }
        backToCurrentWeatherClick()
    }

    private fun buttonClickAnimation() {
        val frameAnimation = binding.buttonGetCurrentWeather.drawable as AnimationDrawable
        frameAnimation.start()
        Handler(Looper.getMainLooper()).postDelayed({
//            binding.buttonGetCurrentWeatherGrey.isVisible = true
//            binding.buttonGetCurrentWeather.isVisible = false
            frameAnimation.stop()
        }, 400)
    }

    //endregion

    //region ========================================= Click Button =========================================

    private fun backToCurrentWeatherClick() {
        binding.backToCurrentWeather.setOnClickListener {
            (activity as MainActivity).backToCurrentWeather()
        }
    }

    private fun getWeatherButtonClick() {
        binding.buttonGetCurrentWeather.setOnClickListener {
//        binding.buttonGetCurrentWeather.isEnabled = false
            buttonClickAnimation()
        }

//        Handler(Looper.getMainLooper()).postDelayed({
//            binding.buttonGetCurrentWeather.isEnabled = true
//            binding.buttonGetCurrentWeatherGrey.isVisible = false
//            binding.buttonGetCurrentWeather.isVisible = true
//        }, 4000)
    }

    //endregion

    //region ======================================= Get Weather Data =======================================

    private fun getCurrentWeatherByCurrentLocation(address: Address?) {
        viewModel.getWeatherStackData(address!!.locality)

        viewModel.weatherStackLiveData.observe((activity as MainActivity), { weatherStackData ->
            binding.cityName.text =
                "${weatherStackData.location.name}, ${weatherStackData.location.country}"

            binding.weatherDescription.text = weatherStackData.current.weather_descriptions[0]
            binding.temperatureValue.text = "${weatherStackData.current.temperature} °C"
            binding.windValue.text = "${weatherStackData.current.wind_speed} km/h"
            binding.humidityValue.text = "${weatherStackData.current.humidity} %"

            Glide.with(this)
                .load(weatherStackData.current.weather_icons[0])
                .centerCrop()
                .into(binding.weatherIcon)
            binding.weatherIcon.visibility = View.VISIBLE
        })
    }

    //endregion

    //region ========================================= Get Location =========================================

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

    private fun trackLocationIfPermissionIsGranted() {
        requireActivity().checkAndRequestPermission(
            Manifest.permission.ACCESS_FINE_LOCATION,
            REQUEST_LOCATION_PERMISSION,
            ::getLocationManager
        )
    }

    //endregion
}