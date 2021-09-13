package com.suonk.weatherreportplus.ui.activities

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import com.suonk.weatherreportplus.R
import com.suonk.weatherreportplus.databinding.ActivityMapsBinding
import com.suonk.weatherreportplus.utils.InjectorUtils
import com.suonk.weatherreportplus.viewmodels.WeatherReportViewModel
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        private const val LOCATION_REQUEST_CODE = 1
    }

    private lateinit var viewModel: WeatherReportViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setUpViewModel()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        setUpMap()
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
            return
        }
        mMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                lastLocation = location

                val currentLatLng = LatLng(lastLocation.latitude, lastLocation.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                getCityFromLatLong(lastLocation.latitude, lastLocation.longitude)
            }
        }
    }

    private fun getCityFromLatLong(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(this, Locale.getDefault())
        val listOfCitiesName = geocoder.getFromLocation(latitude, longitude, 1)
        if (listOfCitiesName.size > 0) {
            getCurrentWeatherByCurrentLocation(listOfCitiesName[0])
        }
    }

    private fun setUpViewModel() {
        val factory = InjectorUtils.provideViewModelFactory()
        viewModel = ViewModelProvider(this, factory)[WeatherReportViewModel::class.java]
    }

    private fun getCurrentWeatherByCurrentLocation(address: Address?) {
        viewModel.getWeatherStackData(address!!.locality)
//        Log.i("getCurrentWeather", address.locality)

        viewModel.weatherStackData.observe(this, { weatherStackData ->
            Log.i("getCurrentWeather", "${weatherStackData.current.temperature}")
            Log.i("getCurrentWeather", "${weatherStackData.current.humidity}")

            binding.weather.text = "À ${address.locality}, il fait ${weatherStackData.current.temperature} °C"
        })

        viewModel.errorMessage.observe(this, { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        })

        viewModel.loading.observe(this, { loadingIsVisible ->
            binding.progressBar.isVisible = loadingIsVisible
        })
    }
}