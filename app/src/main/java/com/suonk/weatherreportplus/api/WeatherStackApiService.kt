package com.suonk.weatherreportplus.api

import com.suonk.weatherreportplus.models.WeatherStackData
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherStackApiService {

    @GET("/current")
    suspend fun getWeatherStackData(
        @Query("access_key") apiKey: String,
        @Query("query") city: String

    ): Response<WeatherStackData>

    // http://api.weatherstack.com /current ?access_key = cae5824328da6d7ce8f7f9a21adeda28 & query=Sevran

    companion object {
        var weatherStackApiService: WeatherStackApiService? = null
        val API_KEY = "cae5824328da6d7ce8f7f9a21adeda28"

        fun getInstance(): WeatherStackApiService {
            if (weatherStackApiService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://api.weatherstack.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                weatherStackApiService = retrofit.create(WeatherStackApiService::class.java)
            }
            return weatherStackApiService!!
        }
    }
}