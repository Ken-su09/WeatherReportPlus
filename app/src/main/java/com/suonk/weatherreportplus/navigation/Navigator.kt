package com.suonk.weatherreportplus.navigation

import androidx.fragment.app.FragmentActivity
import com.suonk.weatherreportplus.R
import com.suonk.weatherreportplus.ui.fragments.CurrentWeatherFragment
import com.suonk.weatherreportplus.ui.fragments.SplashScreenFragment
import com.suonk.weatherreportplus.ui.fragments.WeatherDetailsFragment
import javax.inject.Inject

class Navigator @Inject constructor(var activity: FragmentActivity?) {

    fun showSplashScreen() {
        activity!!.supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container_view, SplashScreenFragment())
            .commit()
    }

    fun showCurrentWeather() {
        activity!!.supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container_view, CurrentWeatherFragment())
            .commit()
    }

    fun showWeatherDetails() {
        activity!!.supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container_view, WeatherDetailsFragment())
            .commit()
    }
}