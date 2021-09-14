package com.suonk.weatherreportplus.utils

import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object CheckAndRequestPermissions {

    fun Activity.checkAndRequestPermission(
        manifestPermission: String, requestCode: Int,
        action: () -> Unit
    ) {
        val permissionStatus =
            ContextCompat.checkSelfPermission(applicationContext, manifestPermission)

        if (permissionStatus == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, manifestPermission)) {
                requestPermission(manifestPermission, requestCode)
            } else {
                requestPermission(manifestPermission, requestCode)
            }
        } else {
            action()
        }
    }

    fun Activity.requestPermission(manifestPermission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(manifestPermission), requestCode)
    }
}