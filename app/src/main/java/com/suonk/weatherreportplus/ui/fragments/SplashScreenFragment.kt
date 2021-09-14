package com.suonk.weatherreportplus.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.suonk.weatherreportplus.R
import com.suonk.weatherreportplus.databinding.FragmentSplashScreenBinding
import com.suonk.weatherreportplus.ui.activities.MainActivity

class SplashScreenFragment : Fragment() {

    private lateinit var binding: FragmentSplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun initializeUI() {
        val animation = AnimationUtils.loadAnimation(context, R.anim.suddenly_appear)
        binding.appLogo.startAnimation(animation)
        binding.appName.startAnimation(animation)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        initializeUI()
        return binding.root
    }
}