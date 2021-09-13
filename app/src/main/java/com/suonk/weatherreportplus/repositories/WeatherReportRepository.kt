package com.suonk.weatherreportplus.repositories

import com.suonk.weatherreportplus.api.WeatherStackApiService
import com.suonk.weatherreportplus.api.WeatherStackApiService.Companion.API_KEY

class WeatherReportRepository constructor(private val weatherStackApiService: WeatherStackApiService) {

    suspend fun getWeatherStackData(city: String) =
        weatherStackApiService.getWeatherStackData(API_KEY, city)

    companion object {
        @Volatile
        private var instance: WeatherReportRepository? = null

        fun getInstance(weatherStackApiService: WeatherStackApiService): WeatherReportRepository =
            instance ?: synchronized(this) {
                instance ?: WeatherReportRepository(weatherStackApiService).also {
                    instance = it
                }
            }
    }
}