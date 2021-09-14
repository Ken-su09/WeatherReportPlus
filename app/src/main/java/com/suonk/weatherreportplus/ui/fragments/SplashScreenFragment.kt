package com.suonk.weatherreportplus.ui.fragments

import android.Manifest
import android.graphics.drawable.AnimationDrawable
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.suonk.weatherreportplus.R
import com.suonk.weatherreportplus.databinding.FragmentSplashScreenBinding
import com.suonk.weatherreportplus.utils.CheckAndRequestPermissions.checkAndRequestPermission
import com.suonk.weatherreportplus.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

class SplashScreenFragment : Fragment() {

    private var binding: FragmentSplashScreenBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        initializeUI()
        return binding!!.root
    }

    private fun initializeUI() {
        appLogoAnimation()
    }

    private fun appLogoAnimation() {
        val animation = AnimationUtils.loadAnimation(context, R.anim.suddenly_appear)
        val frameAnimation = binding!!.appLogo.drawable as AnimationDrawable
        frameAnimation.start()
        binding!!.appName.startAnimation(animation)
        Handler(Looper.getMainLooper()).postDelayed({
            frameAnimation.stop()
        }, 700)
    }

    //region ========================================== Lifecycle ===========================================

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    //endregion
}