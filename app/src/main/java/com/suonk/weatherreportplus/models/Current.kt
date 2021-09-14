package com.suonk.weatherreportplus.models

data class Current(
    val cloudcover: Int,
    val feelslike: Int,
    val humidity: Int,
    val is_day: String,
    val observation_time: String,
    val precip: Double,
    val pressure: Int,
    val temperature: Int,
    val uv_index: Int,
    val visibility: Int,
    val weather_code: Int,
    val weather_descriptions: List<String>,
    val weather_icons: List<String>,
    val wind_degree: Int,
    val wind_dir: String,
    val wind_speed: Int
)