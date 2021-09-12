package com.suonk.weatherreportplus.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.suonk.weatherreportplus.models.CurrentWeather
import com.suonk.weatherreportplus.repositories.WeatherReportRepository
import kotlinx.coroutines.*

class WeatherReportViewModel constructor(private val repository: WeatherReportRepository) :
    ViewModel() {

    var job: Job? = null
    val errorMessage = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    val currentWeather = MutableLiveData<CurrentWeather>()

    fun getCurrentWeatherByCurrentCity(city: String) {
        job = CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
            if (isActive) {
                val response = repository.getCurrentWeatherByCurrentLocation(city)

                if (response.isSuccessful) {
                    currentWeather.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error : ${response.message()}")
                }
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}