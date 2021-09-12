package com.suonk.weatherreportplus.utils

import com.suonk.weatherreportplus.api.WeatherStackApiService
import com.suonk.weatherreportplus.repositories.WeatherReportRepository
import com.suonk.weatherreportplus.viewmodels.WeatherReportViewModelFactory

object InjectorUtils {

    fun provideViewModelFactory(): WeatherReportViewModelFactory {
        val repository = WeatherReportRepository.getInstance(WeatherStackApiService.getInstance())
        return WeatherReportViewModelFactory(repository)
    }
}