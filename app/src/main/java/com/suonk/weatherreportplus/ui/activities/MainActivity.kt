package com.suonk.weatherreportplus.ui.activities

import android.Manifest
import android.location.*
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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

        binding.buttonToMaps.setOnClickListener {
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

        viewModel.weatherStackLiveData.observe(this, { weatherStackData ->
            Log.i("getCurrentWeather", "${weatherStackData.current.temperature}")
            Log.i("getCurrentWeather", "${weatherStackData.current.humidity}")

            binding.weather.text =
                "À ${address.locality}, il fait ${weatherStackData.current.temperature} °C"
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
}