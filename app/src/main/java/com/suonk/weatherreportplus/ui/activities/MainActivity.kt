package com.suonk.weatherreportplus.ui.activities

import android.Manifest
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.suonk.weatherreportplus.databinding.ActivityMainBinding
import com.suonk.weatherreportplus.navigation.Navigator
import com.suonk.weatherreportplus.navigation.WeatherReportFlowCoordinator
import com.suonk.weatherreportplus.utils.CheckAndRequestPermissions.checkAndRequestPermission
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 10100
    }

    private var locationManager: LocationManager? = null

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {
        }
        override fun onProviderDisabled(provider: String) {
        }
    }

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
        trackLocationIfPermissionIsGranted()

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
            Log.d("SecurityException", "Security Exception, no location available")
        }
    }

    private fun trackLocationIfPermissionIsGranted() {
        checkAndRequestPermission(
            Manifest.permission.ACCESS_FINE_LOCATION,
            REQUEST_LOCATION_PERMISSION,
            ::getLocationManager
        )
    }

    //endregion

    override fun onDestroy() {
        super.onDestroy()
        navigator.activity = null
    }
}