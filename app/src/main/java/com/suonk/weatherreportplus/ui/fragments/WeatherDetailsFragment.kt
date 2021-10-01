package com.suonk.weatherreportplus.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.suonk.weatherreportplus.databinding.FragmentWeatherDetailsBinding
import com.suonk.weatherreportplus.ui.activities.MainActivity
import com.suonk.weatherreportplus.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherDetailsFragment : Fragment() {

    //region ========================================== Val or Var ==========================================

    private var binding: FragmentWeatherDetailsBinding? = null
    private val viewModel: SharedViewModel by activityViewModels()

    //endregion

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeatherDetailsBinding.inflate(inflater, container, false)
        initializeUI()

        return binding?.root
    }

    private fun initializeUI() {
        getCurrentWeatherByCurrentLocation()
        backToCurrentWeatherClick()
    }

    //endregion

    //region ========================================= Click Button =========================================

    private fun backToCurrentWeatherClick() {
        binding?.backToCurrentWeather?.setOnClickListener {
            (activity as MainActivity).backToCurrentWeather()
        }
    }

    //endregion

    //region ======================================= Get Weather Data =======================================

    private fun getCurrentWeatherByCurrentLocation() {
        viewModel.locationLiveData.observe(requireActivity(), { location ->
            viewModel.getWeatherStackData(location)
        })

        viewModel.weatherStackLiveData.observe(requireActivity(), { weatherStackData ->
            if (binding != null) {
                binding?.cityName?.text =
                    "${weatherStackData.location.name}, ${weatherStackData.location.country}"

                binding?.weatherDescription?.text = weatherStackData.current.weather_descriptions[0]

                // Weather
                binding?.temperatureValue?.text = "${weatherStackData.current.temperature} °C"
                binding?.temperatureValueFeelsLike?.text =
                    "${weatherStackData.current.feelslike} °C"
                binding?.uvIndexData?.text = "${weatherStackData.current.uv_index}"

                // Wind
                binding?.windValue?.text = "${weatherStackData.current.wind_speed} km/h"
                binding?.windDegreeValue?.text = "${weatherStackData.current.wind_degree} °"

                val windDirectionMap = mapOf(
                    "N" to "North",
                    "NNE" to "North-North-East",
                    "NE" to "North-East",
                    "ENE" to "East-North-East",
                    "E" to "East",
                    "ESE" to "East-South-East",
                    "SE" to "South-East",
                    "SSE" to "South-South-East",
                    "S" to "South",
                    "SSW" to "South-South-West",
                    "SW" to "South-West",
                    "WSW" to "West-South-West",
                    "W" to "West",
                    "WNW" to "West-North-West",
                    "NW" to "North-West",
                    "NNW" to "North-North-West",
                )

                binding?.windDirValue?.text = windDirectionMap[weatherStackData.current.wind_dir]

                // Rain
                binding?.humidityValue?.text = "${weatherStackData.current.humidity} %"
                binding?.precipitationValue?.text = "${weatherStackData.current.precip} %"
                binding?.cloudCoverValue?.text = "${weatherStackData.current.cloudcover} %"
            }
        })
    }

    //endregion

    //region ========================================== Lifecycle ===========================================

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    //endregion
}