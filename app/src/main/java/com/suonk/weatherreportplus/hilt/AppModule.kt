package com.suonk.weatherreportplus.hilt

import com.suonk.weatherreportplus.api.WeatherStackApiService
import com.suonk.weatherreportplus.api.WeatherStackApiService.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    fun provideWeatherStackApiService(retrofit: Retrofit): WeatherStackApiService =
        retrofit.create(WeatherStackApiService::class.java)
}