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
class SharedViewModel
@Inject constructor(private val api: WeatherStackApiService) :
    ViewModel() {

    var job: Job? = null
    val weatherStackLiveData = MutableLiveData<WeatherStackData>()
    val locationLiveData = MutableLiveData<String>()
    val loadingProgressBar = MutableLiveData<Boolean>()

    fun getWeatherStackData(city: String) {
        job = viewModelScope.launch {
            if (isActive) {
                loadingProgressBar.value = true
                val response = api.getWeatherStackData(API_KEY, city)
                delay(3000)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        weatherStackLiveData.postValue(response.body())
                        Log.i("ViewModel", "${response.body()}")
                        Log.i("ViewModel", "$response")
                        loadingProgressBar.value = false
                    } else {
                        Log.i("ViewModel", response.message())
                    }
                }

                job?.cancel()
            }
        }
    }

    fun setLocationLiveData(location: String) {
        locationLiveData.value = location
        Log.i("setLocationLiveData", location)
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}