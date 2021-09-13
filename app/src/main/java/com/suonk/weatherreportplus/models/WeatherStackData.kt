package com.suonk.weatherreportplus.models

data class WeatherStackData(
    val current: Current,
    val location: Location,
    val request: Request
)