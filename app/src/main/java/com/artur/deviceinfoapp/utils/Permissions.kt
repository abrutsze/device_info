package com.artur.deviceinfoapp.utils

import android.Manifest.permission
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*


class Permissions {

    private val TAG = Permissions::class.java.simpleName
    private lateinit var activity: Activity

    fun gimmePermission(activity: Activity): Boolean {
        this.activity = activity
        Log.d(TAG, "canWork= " + canWork())
        return if (!canWork()) {
            ActivityCompat.requestPermissions(
                activity,
                permissions(PERMISSIONS), 10
            )
            false
        } else {
            true
        }
    }

    private fun canWork(): Boolean {
        this.activity = activity
        return (hasPermission(permission.ACCESS_WIFI_STATE) && hasPermission(permission.ACCESS_NETWORK_STATE)
                && hasPermission(permission.ACCESS_FINE_LOCATION) && hasPermission(permission.ACCESS_COARSE_LOCATION))
    }

    private fun hasPermission(permission: String): Boolean {
        return activity.let { ContextCompat.checkSelfPermission(it, permission) } ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun permissions(wanted: Array<String>): Array<String> {
        val result = ArrayList<String>()
        for (perm in wanted) {
            if (!hasPermission(perm)) {
                result.add(perm)
            }
        }
        return result.toTypedArray()
    }

    companion object {
        private val PERMISSIONS = arrayOf(
            permission.ACCESS_WIFI_STATE,
            permission.ACCESS_NETWORK_STATE,
            permission.ACCESS_FINE_LOCATION,
            permission.ACCESS_COARSE_LOCATION
        )
    }
}