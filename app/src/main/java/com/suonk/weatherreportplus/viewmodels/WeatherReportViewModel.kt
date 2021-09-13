package com.suonk.weatherreportplus.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.suonk.weatherreportplus.models.WeatherStackData
import com.suonk.weatherreportplus.repositories.WeatherReportRepository
import kotlinx.coroutines.*

class WeatherReportViewModel constructor(private val repository: WeatherReportRepository) :
    ViewModel() {

    var job: Job? = null
    val errorMessage = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    val weatherStackData = MutableLiveData<WeatherStackData>()

    fun getWeatherStackData(city: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            if (isActive) {
                val response = repository.getWeatherStackData(city)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        weatherStackData.postValue(response.body())
                        Log.i("ViewModel", "${response.body()}")
                        Log.i("ViewModel", "$response")
                        loading.value = false
                    } else {
                        onError("Error : ${response.message()}")
                    }
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