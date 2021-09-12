package com.suonk.weatherreportplus.models

data class CurrentLocation(
    val country: String,
    val lat: String,
    val lon: String,
    val name: String,
    val region: String,
    val timezone_id: String
)