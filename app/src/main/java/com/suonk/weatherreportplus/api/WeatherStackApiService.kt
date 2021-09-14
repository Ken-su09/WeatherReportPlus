package com.suonk.weatherreportplus.api

import com.suonk.weatherreportplus.models.WeatherStackData
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherStackApiService {

    companion object {
        const val BASE_URL = "http://api.weatherstack.com"
        const val API_KEY = "9503e42ce3a1f064bd2b26cc91579c4a"
//        const val API_KEY = "58a14a419e31149fb8ad703a8454efca"
//        const val API_KEY = "cae5824328da6d7ce8f7f9a21adeda28"
    }

    @GET("/current")
    suspend fun getWeatherStackData(
        @Query("access_key") apiKey: String,
        @Query("query") city: String

    ): Response<WeatherStackData>

    // http://api.weatherstack.com /current ?access_key = cae5824328da6d7ce8f7f9a21adeda28 & query=Sevran
}