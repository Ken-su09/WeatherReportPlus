package com.suonk.weatherreportplus.repositories

import com.suonk.weatherreportplus.api.WeatherStackApiService
import com.suonk.weatherreportplus.api.WeatherStackApiService.Companion.API_KEY
import javax.inject.Inject

class WeatherReportRepository @Inject constructor(private val weatherStackApiService: WeatherStackApiService) {

    suspend fun getWeatherStackData(city: String) =
        weatherStackApiService.getWeatherStackData(API_KEY, city)
}