package com.suonk.weatherreportplus.navigation

class WeatherReportFlowCoordinator(private val navigator: Navigator) {

    fun start() {
        navigator.showSplashScreen()
    }

    fun showCurrentWeather() {
        navigator.showCurrentWeather()
    }

    fun showWeatherDetails() {
        navigator.showSplashScreen()
    }
}