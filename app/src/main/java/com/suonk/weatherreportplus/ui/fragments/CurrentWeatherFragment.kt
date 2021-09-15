package com.suonk.weatherreportplus.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.drawable.AnimationDrawable
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.android.gms.location.*
import com.suonk.weatherreportplus.databinding.FragmentCurrentWeatherBinding
import com.suonk.weatherreportplus.ui.activities.MainActivity
import com.suonk.weatherreportplus.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CurrentWeatherFragment : Fragment() {

    //region ========================================== Val or Var ==========================================

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 99
    }

    private var fusedLocationProvider: FusedLocationProviderClient? = null
    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = 30
        fastestInterval = 10
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        maxWaitTime = 60
    }

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.isNotEmpty()) {
                val location = locationList.last()
                binding!!.progressBar.isVisible = (binding!!.weatherDescription.text == "")
                if (cityName == "") {
                    getCityFromLatLong(location.latitude, location.longitude)
                }
            }
        }
    }

    private var cityName = ""
    private val viewModel: SharedViewModel by activityViewModels()
    private var binding: FragmentCurrentWeatherBinding? = null

    //endregion

    //region ============================================== UI ==============================================

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)
        initializeUI()
        return binding!!.root
    }

    private fun initializeUI() {
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(requireActivity())
        checkLocationPermission()

        binding!!.buttonGetCurrentWeather.setOnClickListener {
            checkLocationPermission()
            getWeatherButtonClick()
        }
        moreDetailsButtonClick()
    }

    private fun initProgressBar() {
        viewModel.loadingProgressBar.observe(viewLifecycleOwner, { isVisible ->
            binding!!.progressBar.isVisible = isVisible
            binding!!.weatherIcon.isVisible = !isVisible
        })
    }

    private fun buttonClickAnimation() {
        val frameAnimation = binding!!.buttonGetCurrentWeather.drawable as AnimationDrawable
        frameAnimation.start()
        Handler(Looper.getMainLooper()).postDelayed({
            binding!!.buttonGetCurrentWeatherGrey.isVisible = true
            binding!!.buttonGetCurrentWeather.isVisible = false
            frameAnimation.stop()
        }, 400)
    }

    //endregion

    //region ========================================= Click Button =========================================

    private fun moreDetailsButtonClick() {
        binding!!.moreDetailsButton.setOnClickListener {
            (activity as MainActivity).goToWeatherDetail()
        }
    }

    private fun getWeatherButtonClick() {
        binding!!.buttonGetCurrentWeather.isEnabled = false
        buttonClickAnimation()
        initProgressBar()
        getCurrentWeatherByCurrentLocation(cityName)

        Handler(Looper.getMainLooper()).postDelayed({
            if (binding != null) {
                binding!!.buttonGetCurrentWeather.isEnabled = true
                binding!!.buttonGetCurrentWeatherGrey.isVisible = false
                binding!!.buttonGetCurrentWeather.isVisible = true
            }
        }, 4000)
    }

    //endregion

    //region ======================================= Get Weather Data =======================================

    private fun getCurrentWeatherByCurrentLocation(cityName: String) {
        if (cityName != "") {
            viewModel.getWeatherStackData(cityName)

            binding!!.cityName.text = "City : $cityName"
            viewModel.setLocationLiveData(cityName)

            viewModel.weatherStackLiveData.observe(viewLifecycleOwner, { weatherStackData ->
                Log.i("getCurrentWeather", "${weatherStackData.current.temperature}")
                Log.i("getCurrentWeather", "${weatherStackData.current.humidity}")

                binding!!.weatherDescription.text = weatherStackData.current.weather_descriptions[0]
                binding!!.temperatureValue.text = "${weatherStackData.current.temperature} °C"
                binding!!.windValue.text = "${weatherStackData.current.wind_speed} km/h"
                binding!!.humidityValue.text = "${weatherStackData.current.humidity} %"

                Glide.with(this)
                    .load(weatherStackData.current.weather_icons[0])
                    .centerCrop()
                    .into(binding!!.weatherIcon)
                binding!!.weatherIcon.visibility = View.VISIBLE
                binding!!.moreDetailsButton.isVisible = true
            })

        }
    }

    //endregion

    //region ========================================= Get Location =========================================

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {

            fusedLocationProvider?.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    override fun onPause() {
        super.onPause()
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {

            fusedLocationProvider?.removeLocationUpdates(locationCallback)
        }
    }

    //region ========================================= Get Location =========================================

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    (activity as MainActivity),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                requestLocationPermission()
            } else {
                requestLocationPermission()
            }
        }
    }

    private fun requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ),
                REQUEST_LOCATION_PERMISSION
            )
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            requireActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        fusedLocationProvider?.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            Looper.getMainLooper()
                        )
                    }

                } else {
                    Toast.makeText(context, "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    //endregion


    private fun getCityFromLatLong(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(context, Locale.getDefault())
        val listOfCitiesName = geocoder.getFromLocation(latitude, longitude, 1)
        if (listOfCitiesName.size > 0) {
            cityName = listOfCitiesName[0].locality
            getCurrentWeatherByCurrentLocation(cityName)
        }
    }

    //endregion

    //region ========================================== Lifecycle ===========================================

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    //endregion
}