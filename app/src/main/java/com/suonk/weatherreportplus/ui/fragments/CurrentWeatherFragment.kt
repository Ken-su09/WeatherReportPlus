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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.suonk.weatherreportplus.databinding.FragmentCurrentWeatherBinding
import com.suonk.weatherreportplus.ui.activities.MainActivity
import com.suonk.weatherreportplus.utils.CheckAndRequestPermissions.checkAndRequestPermission
import com.suonk.weatherreportplus.viewmodels.WeatherReportViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CurrentWeatherFragment : Fragment() {

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

    private var cityName = ""
    private val viewModel: WeatherReportViewModel by activityViewModels()
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
        locationManager =
            requireActivity().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager?

        trackLocationIfPermissionIsGranted()

        binding!!.buttonGetCurrentWeather.setOnClickListener {
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
            Log.i("moreDetailsButtonClick", "${(activity as MainActivity).goToWeatherDetail()}")
        }
    }

    private fun getWeatherButtonClick() {
        binding!!.buttonGetCurrentWeather.isEnabled = false
        buttonClickAnimation()
        initProgressBar()
        getCurrentWeatherByCurrentLocation(cityName)

        Handler(Looper.getMainLooper()).postDelayed({
            binding!!.buttonGetCurrentWeather.isEnabled = true
            binding!!.buttonGetCurrentWeatherGrey.isVisible = false
            binding!!.buttonGetCurrentWeather.isVisible = true
        }, 4000)
    }

    //endregion

    //region ======================================= Get Weather Data =======================================

    private fun getCurrentWeatherByCurrentLocation(cityName: String) {
        viewModel.getWeatherStackData(cityName)

        binding!!.cityName.text = "City : $cityName"
        viewModel.setLocationLiveData(cityName)

        viewModel.weatherStackLiveData.observe(this, { weatherStackData ->
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
            cityName = listOfCitiesName[0].locality
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

    //region ========================================= Get Location =========================================

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    //endregion
}