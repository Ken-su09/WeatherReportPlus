package com.suonk.weatherreportplus.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suonk.weatherreportplus.api.WeatherStackApiService
import com.suonk.weatherreportplus.api.WeatherStackApiService.Companion.API_KEY
import com.suonk.weatherreportplus.models.WeatherStackData
import com.suonk.weatherreportplus.repositories.WeatherReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class SharedViewModel
@Inject constructor(private val repository: WeatherReportRepository) :
    ViewModel() {

    val weatherStackLiveData = MutableLiveData<WeatherStackData>()
    val locationLiveData = MutableLiveData<String>()
    val loadingProgressBar = MutableLiveData<Boolean>()

    fun getWeatherStackData(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isActive) {
                withContext(Dispatchers.Main) {
                    loadingProgressBar.value = true
                }
                val response = repository.getWeatherStackData(city)
                if (response.isSuccessful) {
                    weatherStackLiveData.postValue(response.body())
                    withContext(Dispatchers.Main) {
                        loadingProgressBar.value = false
                    }
                } else {
                    Log.i("ViewModel", response.message())
                }
            }
        }
    }

    fun setLocationLiveData(location: String) {
        locationLiveData.value = location
        Log.i("setLocationLiveData", location)
    }
}