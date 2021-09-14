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
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.suonk.weatherreportplus.databinding.FragmentWeatherDetailsBinding
import com.suonk.weatherreportplus.ui.activities.MainActivity
import com.suonk.weatherreportplus.utils.CheckAndRequestPermissions.checkAndRequestPermission
import com.suonk.weatherreportplus.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class WeatherDetailsFragment : Fragment() {

    //region ========================================== Val or Var ==========================================

    private var binding: FragmentWeatherDetailsBinding? = null
    private val viewModel: SharedViewModel by activityViewModels()

    //endregion

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherDetailsBinding.inflate(inflater, container, false)
        initializeUI()

        return binding!!.root
    }

    private fun initializeUI() {
        getCurrentWeatherByCurrentLocation()
        backToCurrentWeatherClick()
    }

    //endregion

    //region ========================================= Click Button =========================================

    private fun backToCurrentWeatherClick() {
        binding!!.backToCurrentWeather.setOnClickListener {
            (activity as MainActivity).backToCurrentWeather()
        }
    }

    //endregion

    //region ======================================= Get Weather Data =======================================

    private fun getCurrentWeatherByCurrentLocation() {
        viewModel.locationLiveData.observe(requireActivity(), { location ->
            Log.i("locationLiveData", location)
            viewModel.getWeatherStackData(location)
        })

        viewModel.weatherStackLiveData.observe(requireActivity(), { weatherStackData ->
            binding!!.cityName.text =
                "${weatherStackData.location.name}, ${weatherStackData.location.country}"

            binding!!.weatherDescription.text = weatherStackData.current.weather_descriptions[0]

            // Weather
            binding!!.temperatureValue.text = "${weatherStackData.current.temperature} °C"
            binding!!.temperatureValueFeelsLike.text = "${weatherStackData.current.feelslike} °C"
            binding!!.uvIndexData.text = "${weatherStackData.current.uv_index}"

            // Wind
            binding!!.windValue.text = "${weatherStackData.current.wind_speed} km/h"
            binding!!.windDegreeValue.text = "${weatherStackData.current.wind_degree} °"
            when (weatherStackData.current.wind_dir) {
                "N" -> {
                    binding!!.windDirValue.text = "North"
                }
                "NNE" -> {
                    binding!!.windDirValue.text = "North-North-East"
                }
                "NE" -> {
                    binding!!.windDirValue.text = "North-East"
                }
                "ENE" -> {
                    binding!!.windDirValue.text = "East-North-East"
                }
                "E" -> {
                    binding!!.windDirValue.text = "East"
                }
                "ESE" -> {
                    binding!!.windDirValue.text = "East-South-East"
                }
                "SE" -> {
                    binding!!.windDirValue.text = "South-East"
                }
                "SSE" -> {
                    binding!!.windDirValue.text = "South-South-East"
                }
                "S" -> {
                    binding!!.windDirValue.text = "South"
                }
                "SSW" -> {
                    binding!!.windDirValue.text = "South-South-West"
                }
                "SW" -> {
                    binding!!.windDirValue.text = "South-West"
                }
                "WSW" -> {
                    binding!!.windDirValue.text = "West-South-West"
                }
                "W" -> {
                    binding!!.windDirValue.text = "West"
                }
                "WNW" -> {
                    binding!!.windDirValue.text = "West-North-West"
                }
                "NW" -> {
                    binding!!.windDirValue.text = "North-West"
                }
                "NNW" -> {
                    binding!!.windDirValue.text = "North-North-West"
                }
            }

            // Rain
            binding!!.humidityValue.text = "${weatherStackData.current.humidity} %"
            binding!!.precipitationValue.text = "${weatherStackData.current.precip} %"
            binding!!.cloudCoverValue.text = "${weatherStackData.current.cloudcover} %"

//            binding!!.progressBar.visibility = View.VISIBLE
        })
    }

    //endregion
}