package com.suonk.weatherreportplus.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suonk.weatherreportplus.api.WeatherStackApiService
import com.suonk.weatherreportplus.api.WeatherStackApiService.Companion.API_KEY
import com.suonk.weatherreportplus.models.WeatherStackData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class WeatherReportViewModel
@Inject constructor(private val api: WeatherStackApiService) :
    ViewModel() {

    val weatherStackLiveData = MutableLiveData<WeatherStackData>()

    fun getWeatherStackData(city: String) {
        viewModelScope.launch {
            if (isActive) {
                val response = api.getWeatherStackData(API_KEY, city)
//                delay(3000)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        weatherStackLiveData.postValue(response.body())
                        Log.i("ViewModel", "${response.body()}")
                        Log.i("ViewModel", "$response")
                    } else {
                        Log.i("ViewModel", response.message())
                    }
                }
            }
        }
    }
}