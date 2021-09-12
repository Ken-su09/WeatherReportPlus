package com.suonk.weatherreportplus.models

data class CurrentWeather(
    val cloudcover: Int,
    val humidity: Int,
    val observation_time: String,
    val precip: Int,
    val pressure: Int,
    val temperature: Int,
    val weather_code: Int,
    val weather_descriptions: List<String>,
    val weather_icons: List<String>,
    val wind_degree: Int,
    val wind_dir: String,
    val wind_speed: Int
)