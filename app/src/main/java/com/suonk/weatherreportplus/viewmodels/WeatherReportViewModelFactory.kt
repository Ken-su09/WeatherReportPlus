package com.suonk.weatherreportplus.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.suonk.weatherreportplus.repositories.WeatherReportRepository

class WeatherReportViewModelFactory constructor(private val repository: WeatherReportRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherReportViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherReportViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}